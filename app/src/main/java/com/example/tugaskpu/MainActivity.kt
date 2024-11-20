package com.example.tugaskpu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynote.database.Note
import com.example.mynote.database.NoteDao
import com.example.mynote.database.NoteRoomDatabase
import com.example.tugaskpu.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private var updateId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()
        executorService = Executors.newSingleThreadExecutor()
        //aktifin db
        val db = NoteRoomDatabase.getDatabase(this)
        //hubngin ke notedao biar bisa use method
        mNotesDao = db!!.noteDao()!!

        with(binding) {


            btnTambahdata.setOnClickListener{
                startActivity(Intent(this@MainActivity, TambahData::class.java))
            }

            btnLogout.setOnClickListener {
                prefManager.setLoggedIn(false)
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (!isLoggedIn) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun getAllNotes() {
        with(binding) {
            binding.rvPemilih.layoutManager = LinearLayoutManager(this@MainActivity)
            mNotesDao.allNotes.observe(this@MainActivity) { pemilih ->
                val adapter = PemilihAdapter(
                    pemilih,
                    onDeleteClick = { position ->
                        val note = (rvPemilih.adapter as PemilihAdapter).listPemilih[position]
                        executorService.execute {
                            mNotesDao.delete(note)
                        }
                    },
                    onEditClick = { position ->
                        val pemilih = (rvPemilih.adapter as PemilihAdapter).listPemilih[position]
                        val intent = Intent(this@MainActivity, Edit::class.java)
                        intent.putExtra("note_id", pemilih.id)
                        startActivity(intent)
                    },
                    onViewClick = {position ->
                        val pemilih = (rvPemilih.adapter as PemilihAdapter).listPemilih[position]
                        val intent = Intent(this@MainActivity, LihatData::class.java)
                        intent.putExtra("note_id", pemilih.id)
                        startActivity(intent)
                    }
                )
                binding.rvPemilih.adapter = adapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }

}