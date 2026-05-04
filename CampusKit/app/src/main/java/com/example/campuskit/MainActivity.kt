package com.example.campuskit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campuskit.adapter.TaskAdapter
import com.example.campuskit.database.DatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var tvEmptyState: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewTasks)
        tvEmptyState = findViewById(R.id.tvEmptyState)

        // hubungin ID tombol baru
        val btnAdd: TextView = findViewById(R.id.btnTambahTugas)
        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        // Fungsi ini dipanggil setiap kali halaman ini muncul di layar
        loadTasks()
    }

    private fun loadTasks() {
        val taskList = dbHelper.getAllTasks()
        if (taskList.isEmpty()) {
            tvEmptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            tvEmptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            adapter = TaskAdapter(taskList)
            recyclerView.adapter = adapter
        }
    }
}