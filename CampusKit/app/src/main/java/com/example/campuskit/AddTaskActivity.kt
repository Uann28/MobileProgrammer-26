package com.example.campuskit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campuskit.database.DatabaseHelper
import com.example.campuskit.model.Task

class AddTaskActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        dbHelper = DatabaseHelper(this)

        val etTitle = findViewById<EditText>(R.id.etTaskTitle)
        val etDesc = findViewById<EditText>(R.id.etTaskDesc)
        val etDeadline = findViewById<EditText>(R.id.etTaskDeadline)
        val btnSave = findViewById<Button>(R.id.btnSaveTask)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val desc = etDesc.text.toString().trim()
            val deadline = etDeadline.text.toString().trim()

            if (title.isEmpty() || desc.isEmpty() || deadline.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi ya!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simpan ke database
            val task = Task(title = title, description = desc, deadline = deadline)
            val status = dbHelper.addTask(task)

            if (status > -1) {
                Toast.makeText(this, "Tugas berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Gagal menyimpan tugas.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}