package com.example.tugasakhirpasien

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirpasien.adapter.PasienAdapter
import com.example.tugasakhirpasien.model.Pasien
import com.example.tugasakhirpasien.network.RetrofitClient
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

    private lateinit var rvPasien: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: PasienAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tvWelcomeName = findViewById<TextView>(R.id.tvWelcomeName)
        rvPasien = findViewById(R.id.rvPasien)
        progressBar = findViewById(R.id.progressBar)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val name = intent.getStringExtra(EXTRA_NAME) ?: "User"
        tvWelcomeName.text = "Halo, $name!"

        // Setup Adapter
        adapter = PasienAdapter(
            onEditClick = { pasien ->
                val intent = Intent(this, EditPasienActivity::class.java).apply {
                    putExtra("ID", pasien.id)
                    putExtra("NAMA", pasien.nama)
                    putExtra("TGL_LAHIR", pasien.tanggal_lahir)
                    putExtra("JK", pasien.jenis_kelamin)
                    putExtra("ALAMAT", pasien.alamat)
                    putExtra("TELP", pasien.no_telepon)
                }
                startActivity(intent)
            },
            onDeleteClick = { pasien ->
                showDeleteConfirmation(pasien)
            }
        )

        rvPasien.layoutManager = LinearLayoutManager(this)
        rvPasien.adapter = adapter

        loadDataPasien()

        btnLogout.setOnClickListener { logout() }
    }

    // Refresh data
    override fun onResume() {
        super.onResume()
        loadDataPasien()
    }

    private fun showDeleteConfirmation(pasien: Pasien) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Data")
            .setMessage("Yakin ingin menghapus ${pasien.nama}?")
            .setPositiveButton("Hapus") { dialog, _ ->
                deletePasien(pasien.id)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun deletePasien(id: Int) {
        val token = getSharedPreferences("auth", MODE_PRIVATE).getString("token", "") ?: ""
        if (token.isEmpty()) return

        lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE
            try {
                val response = RetrofitClient.apiService.deletePasien("Bearer $token", id)
                if (response.isSuccessful) {
                    Toast.makeText(this@HomeActivity, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                    loadDataPasien() // Refresh data
                } else {
                    Toast.makeText(this@HomeActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun loadDataPasien() {
        val token = getSharedPreferences("auth", MODE_PRIVATE).getString("token", "") ?: ""
        if (token.isEmpty()) {
            Toast.makeText(this, "Sesi habis, silakan login ulang", Toast.LENGTH_SHORT).show()
            logout()
            return
        }

        lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE
            try {
                val response = RetrofitClient.apiService.getPasien("Bearer $token")
                if (response.isSuccessful) {
                    val dataPasien = response.body()?.data ?: emptyList<Pasien>()
                    adapter.setData(dataPasien)
                } else {
                    Toast.makeText(this@HomeActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun logout() {
        getSharedPreferences("auth", MODE_PRIVATE).edit().remove("token").apply()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}