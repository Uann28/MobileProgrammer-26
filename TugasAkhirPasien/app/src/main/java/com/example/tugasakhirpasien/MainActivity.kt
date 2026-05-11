package com.example.tugasakhirpasien

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tugasakhirpasien.model.LoginRequest
import com.example.tugasakhirpasien.network.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)

        btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE
            btnLogin.isEnabled = false

            try {
                val request = LoginRequest(email, password)
                val response = RetrofitClient.apiService.login(request)

                if (response.isSuccessful) {
                    val loginData = response.body()?.data
                    val userName = loginData?.user?.name.orEmpty()
                    val token = loginData?.token.orEmpty()

                    if (userName.isNotEmpty()) {
                        // Simpan Token
                        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
                        prefs.edit().putString("token", token).apply()

                        // Pindah ke HomeActivity bawa Nama
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        intent.putExtra(HomeActivity.EXTRA_NAME, userName)
                        startActivity(intent)
                        finish() // Tutup halaman login
                    } else {
                        Toast.makeText(this@MainActivity, "Data user tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMessage = response.body()?.message ?: "Email atau password salah"
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error terhubung ke server: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
                btnLogin.isEnabled = true
            }
        }
    }
}