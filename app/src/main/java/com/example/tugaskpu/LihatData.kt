package com.example.tugaskpu

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.mynote.database.NoteDao
import com.example.mynote.database.NoteRoomDatabase
import com.example.tugaskpu.databinding.ActivityEditBinding
import com.example.tugaskpu.databinding.ActivityLihatDataBinding
import com.example.tugaskpu.databinding.ActivityTambahDataBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LihatData : AppCompatActivity() {
    private lateinit var binding: ActivityLihatDataBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private var noteId: Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLihatDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!
//        val db = NoteRoomDatabase.getDatabase(this@Edit)?.noteDao()!!
        noteId = intent.getIntExtra("note_id", 0)

        loadPemilihData()
        with(binding){
            btnKembaliLihat.setOnClickListener(){
                startActivity(Intent(this@LihatData,MainActivity::class.java))
            }
        }

    }
    private fun loadPemilihData() {
        lifecycleScope.launch (Dispatchers.IO) {
            mNotesDao.getVoterById(noteId).collect { pemilih ->
                if (pemilih != null) {
                    withContext(Dispatchers.Main) {
                        binding.etNamaLihat.setText(pemilih.nama_pemilih)
                        binding.etNikLihat.setText(pemilih.nik)
                        binding.etGenderLihat.setText(pemilih.gender)
                        binding.etAlamatLihat.setText(pemilih.alamat)
                    }
                }
            }
        }
    }
}