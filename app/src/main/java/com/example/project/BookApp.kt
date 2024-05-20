package com.example.project

import android.app.Application
import androidx.room.Room
import com.example.project.data.database.TaskDatabase
import com.example.project.data.repository.BookRepo

class BookApp : Application() {
    lateinit var repo: BookRepo

    override fun onCreate() {
        super.onCreate()
        val database = Room.databaseBuilder(
            this,
            TaskDatabase::class.java,
            TaskDatabase.NAME
        ).fallbackToDestructiveMigration().build()

        repo = BookRepo(database.taskDao())
    }
}