package com.example.tugasakhirpasien

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tugasakhirpasien.model.Pasien
import com.example.tugasakhirpasien.network.RetrofitClient
import kotlinx.coroutines.launch

class EditPasienActivity : AppCompatActivity() {

    private var pasienId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pasien)

        val etNama = findViewById<EditText>(R.id.etNama)
        val etTanggalLahir = findViewById<EditText>(R.id.etTanggalLahir)
        val etJenisKelamin = findViewById<EditText>(R.id.etJenisKelamin)
        val etAlamat = findViewById<EditText>(R.id.etAlamat)
        val etNoTelepon = findViewById<EditText>(R.id.etNoTelepon)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Ambil data dari Intent
        pasienId = intent.getIntExtra("ID", 0)
        etNama.setText(intent.getStringExtra("NAMA"))
        etTanggalLahir.setText(intent.getStringExtra("TGL_LAHIR"))
        etJenisKelamin.setText(intent.getStringExtra("JK"))
        etAlamat.setText(intent.getStringExtra("ALAMAT"))
        etNoTelepon.setText(intent.getStringExtra("TELP"))

        btnUpdate.setOnClickListener {
            val token = getSharedPreferences("auth", MODE_PRIVATE).getString("token", "") ?: ""
            if (token.isEmpty()) return@setOnClickListener

            val pasienUpdate = Pasien(
                id = pasienId,
                nama = etNama.text.toString(),
                tanggal_lahir = etTanggalLahir.text.toString(),
                jenis_kelamin = etJenisKelamin.text.toString(),
                alamat = etAlamat.text.toString(),
                no_telepon = etNoTelepon.text.toString()
            )

            lifecycleScope.launch {
                progressBar.visibility = View.VISIBLE
                try {
                    val response = RetrofitClient.apiService.updatePasien("Bearer $token", pasienId, pasienUpdate)
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditPasienActivity, "Berhasil update data!", Toast.LENGTH_SHORT).show()
                        finish() // Kembali ke HomeActivity
                    } else {
                        Toast.makeText(this@EditPasienActivity, "Gagal update!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@EditPasienActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}