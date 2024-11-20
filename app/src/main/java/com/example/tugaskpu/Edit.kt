package com.example.tugaskpu

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.mynote.database.Note
import com.example.mynote.database.NoteDao
import com.example.mynote.database.NoteRoomDatabase
import com.example.tugaskpu.databinding.ActivityEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Edit : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private lateinit var executorService: ExecutorService
    private lateinit var noteDao: NoteDao
    private var noteId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        noteDao = db!!.noteDao()!!
//        val db = NoteRoomDatabase.getDatabase(this@Edit)?.noteDao()!!
        noteId = intent.getIntExtra("note_id", 0)

        loadPemilihData()
        with(binding) {
            btnSimpanTambah.setOnClickListener(View.OnClickListener {
                updatedata()
            })
//
//            binding.etNamaEdit.text = namadpn.
//            binding.txtNama.text = "Name : " + namadpn +" " +namablkng


        }
    }

    private fun updatedata() {
        val selectedGender = when (binding.radioGroupGender.checkedRadioButtonId) {
            binding.radioLaki.id -> "Laki-laki"
            binding.radioWanita.id -> "Perempuan"
            else -> ""
        }
        val nama_pemilih = binding.etNamaEdit.text.toString()
        val nik = binding.etNikEdit.text.toString()
        val gender = selectedGender
        val alamat = binding.etAlamat.text.toString()
        if (validateInput(nama_pemilih, nik, alamat)) {
            lifecycleScope.launch(Dispatchers.IO) {
                noteDao.update(
                    Note(
                        id = noteId,
                        nama_pemilih = nama_pemilih,
                        nik = nik,
                        gender = gender,
                        alamat = alamat
                    )
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Edit, "Data updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }else{
            Toast.makeText(this@Edit, "Isi semua field terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(namaPemilih: String, nik: String, alamat: String): Boolean {
        return namaPemilih.isNotBlank() && nik.isNotBlank() && alamat.isNotBlank()
    }

    private fun loadPemilihData() {
        lifecycleScope.launch (Dispatchers.IO) {
                noteDao.getVoterById(noteId).collect { pemilih ->
                    if (pemilih != null) {
                        withContext(Dispatchers.Main) {
                            binding.etNamaEdit.setText(pemilih.nama_pemilih)
                            binding.etNikEdit.setText(pemilih.nik)
                            if (pemilih.gender == "Laki-laki") {
                                binding.radioLaki.isChecked = true
                            } else {
                                binding.radioWanita.isChecked = true
                            }
                            binding.etAlamat.setText(pemilih.alamat)
                        }
                    }
                }
        }
    }
}

