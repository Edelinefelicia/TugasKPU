package com.example.tugaskpu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mynote.database.Note
import com.example.mynote.database.NoteDao
import com.example.mynote.database.NoteRoomDatabase
import com.example.tugaskpu.databinding.ActivityTambahDataBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TambahData : AppCompatActivity() {
    private lateinit var binding: ActivityTambahDataBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private var updateId: Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityTambahDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        //aktifin db
        val db = NoteRoomDatabase.getDatabase(this)
        //hubngin ke notedao biar bisa use method
        mNotesDao = db!!.noteDao()!!

        with(binding) {
            btnSimpanTambah.setOnClickListener(View.OnClickListener {
                val nama_pemilih = binding.etNamaTmbh.text.toString()
                val nik = binding.etNikTmbh.text.toString()
                val alamat = binding.etAlamat.text.toString()
                val selectedGender = when (binding.radioGroupGender.checkedRadioButtonId) {
                    binding.radioLaki.id -> "Laki-laki"
                    binding.radioWanita.id -> "Perempuan"
                    else -> ""
                }
                if (validateInput(nama_pemilih, nik, alamat)) {
                    insert(
                        Note(
                            nama_pemilih = nama_pemilih,
                            nik = nik,
                            gender = selectedGender,
                            alamat = alamat
                        )
                    )
                    startActivity(Intent(this@TambahData,MainActivity::class.java))

                }else{
                    Toast.makeText(this@TambahData, "Isi semua field terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
                setEmptyField()
            })
        }
    }
    private fun validateInput(namaPemilih: String, nik: String, alamat: String): Boolean {
        return namaPemilih.isNotBlank() && nik.isNotBlank() && alamat.isNotBlank()
    }
    private fun insert(note: Note) {
        executorService.execute { mNotesDao.insert(note) }
    }
    private fun setEmptyField() {
        with(binding){
            etNamaTmbh.setText("")
            etNikTmbh.setText("")
            etAlamat.setText("")
        }
    }
}