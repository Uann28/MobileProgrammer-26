package com.example.studentprofileapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etNama = findViewById<EditText>(R.id.etNama)
        val etNim = findViewById<EditText>(R.id.etNim)
        val spinnerProdi = findViewById<Spinner>(R.id.spinnerProdi)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val cbCoding = findViewById<CheckBox>(R.id.cbCoding)
        val cbDesain = findViewById<CheckBox>(R.id.cbDesain)
        val cbGame = findViewById<CheckBox>(R.id.cbGame)
        val cbOlahraga = findViewById<CheckBox>(R.id.cbOlahraga)
        val btnSubmit = findViewById<Button>(R.id.btnTampilkanProfil)

        btnSubmit.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val nim = etNim.text.toString().trim()

            // Validasi Input
            if (nama.isEmpty()) {
                etNama.error = "Nama tidak boleh kosong!"
                etNama.requestFocus()
                return@setOnClickListener
            }

            if (nim.isEmpty()) {
                etNim.error = "NIM tidak boleh kosong!"
                etNim.requestFocus()
                return@setOnClickListener
            }

            val prodi = spinnerProdi.selectedItem.toString()

            val selectedGenderId = rgGender.checkedRadioButtonId
            val radioButtonGender = findViewById<RadioButton>(selectedGenderId)
            val jenisKelamin = radioButtonGender.text.toString()

            val hobbies = mutableListOf<String>()
            if (cbCoding.isChecked) hobbies.add("Coding")
            if (cbDesain.isChecked) hobbies.add("Desain")
            if (cbGame.isChecked) hobbies.add("Game")
            if (cbOlahraga.isChecked) hobbies.add("Olahraga")

            val hobiResult = if (hobbies.isNotEmpty()) hobbies.joinToString(", ") else "Tidak ada hobi dipilih"

            // Mengirim data
            val userProfile = UserProfile(nama, nim, prodi, jenisKelamin, hobiResult)
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            intent.putExtra("EXTRA_USER_DATA", userProfile)
            startActivity(intent)
        }
    }
}