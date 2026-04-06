package com.example.studentprofileapp

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tvNama = findViewById<TextView>(R.id.tvProfilNama)
        val tvNim = findViewById<TextView>(R.id.tvProfilNim)
        val tvProdi = findViewById<TextView>(R.id.tvProfilProdi)
        val tvGender = findViewById<TextView>(R.id.tvProfilGender)
        val tvHobi = findViewById<TextView>(R.id.tvProfilHobi)

        // Menerima data
        val userProfile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_USER_DATA", UserProfile::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<UserProfile>("EXTRA_USER_DATA")
        }

        if (userProfile != null) {
            tvNama.text = "Nama: ${userProfile.nama}"
            tvNim.text = "NIM: ${userProfile.nim}"
            tvProdi.text = "Program Studi: ${userProfile.prodi}"
            tvGender.text = "Jenis Kelamin: ${userProfile.jenisKelamin}"
            tvHobi.text = "Hobi: ${userProfile.hobi}"
        }
    }
}