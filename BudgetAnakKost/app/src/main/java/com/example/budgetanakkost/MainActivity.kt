package com.example.budgetanakkost

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    // Deklarasi variabel
    private lateinit var inputGaji: EditText
    private lateinit var inputBiayaTetap: EditText
    private lateinit var inputTabungan: EditText
    private lateinit var textHasil: TextView
    private lateinit var radioGroupPeriode: RadioGroup // Tambahan variabel untuk Pilihan Mingguan/Bulanan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Menyambungkan ID dari XML ke Kotlin
        inputGaji = findViewById(R.id.inputGaji)
        inputBiayaTetap = findViewById(R.id.inputBiayaTetap)
        inputTabungan = findViewById(R.id.inputTabungan)
        textHasil = findViewById(R.id.textHasil)
        radioGroupPeriode = findViewById(R.id.radioGroupPeriode)
    }

    // Fungsi onClick
    fun hitungJatahHarian(view: View) {
        val strGaji = inputGaji.text.toString()
        val strBiayaTetap = inputBiayaTetap.text.toString()
        val strTabungan = inputTabungan.text.toString()

        // Validasi jika ada yang kosong
        if (strGaji.isEmpty() || strBiayaTetap.isEmpty() || strTabungan.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom! (Isi angka 0 jika tidak ada)", Toast.LENGTH_SHORT).show()
            return
        }

        val gaji = strGaji.toDouble()
        val biayaTetap = strBiayaTetap.toDouble()
        val tabungan = strTabungan.toDouble()

        val sisaUang = gaji - biayaTetap - tabungan

        // Validasi minus
        if (sisaUang < 0) {
            textHasil.text = "Peringatan: Pengeluaran dan tabunganmu melebihi uang yang kamu miliki!"
            textHasil.setTextColor(getColor(android.R.color.holo_red_dark))
            return
        }

        // LOGIKA BARU: Cek pilihan Radio Button (Mingguan = bagi 7, Bulanan = bagi 30)
        val pembagiHari = if (radioGroupPeriode.checkedRadioButtonId == R.id.radioMingguan) {
            7 // Jika Mingguan dipilih
        } else {
            30 // Jika Bulanan dipilih (atau jadi default)
        }

        val jatahHarian = sisaUang / pembagiHari

        // Menyesuaikan teks hasil akhir berdasarkan periode
        val teksPeriode = if (pembagiHari == 7) "minggu" else "bulan"

        val hasilAkhir = """
            Sisa uang bersih (per $teksPeriode): Rp ${sisaUang.roundToInt()}
            
            Maksimal jatah hidup kamu:
            Rp ${jatahHarian.roundToInt()} / hari
        """.trimIndent()

        textHasil.setTextColor(getColor(android.R.color.holo_green_dark))
        textHasil.text = hasilAkhir
    }
}