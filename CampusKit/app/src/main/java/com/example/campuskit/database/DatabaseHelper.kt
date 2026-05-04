package com.example.campuskit.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.campuskit.model.Task

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CampusKitDB"
        private const val TABLE_TASKS = "tasks"

        // Nama-nama kolom di tabel
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_DESC = "description"
        private const val KEY_DEADLINE = "deadline"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Membuat tabel SQLite
        val createTable = ("CREATE TABLE $TABLE_TASKS ("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_TITLE TEXT,"
                + "$KEY_DESC TEXT,"
                + "$KEY_DEADLINE TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Menghapus tabel lama jika ada versi baru, lalu buat ulang
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    // FUNGSI CREATE: Menyimpan data tugas baru ke database
    fun addTask(task: Task): Long {
        val db = this.writableDatabase
        val values = ContentValues()

        // Memasukkan data ke masing-masing kolom
        values.put(KEY_TITLE, task.title)
        values.put(KEY_DESC, task.description)
        values.put(KEY_DEADLINE, task.deadline)

        // Insert ke database dan tutup koneksi
        val success = db.insert(TABLE_TASKS, null, values)
        db.close()
        return success
    }

    // FUNGSI READ: Mengambil semua data tugas dari database
    fun getAllTasks(): ArrayList<Task> {
        val taskList = ArrayList<Task>()
        val selectQuery = "SELECT  * FROM $TABLE_TASKS"
        val db = this.readableDatabase

        var cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (_: Exception) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var title: String
        var desc: String
        var deadline: String

        // Membaca data baris per baris
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE))
                desc = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC))
                deadline = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DEADLINE))

                val task = Task(id = id, title = title, description = desc, deadline = deadline)
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return taskList
    }
}