package com.example.project.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.project.data.model.Converter
import com.example.project.data.model.Task

@Database(entities = [Task::class], version = 2)
@TypeConverters(Converter::class)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object{
        const val NAME = "my_task_database"
    }
}