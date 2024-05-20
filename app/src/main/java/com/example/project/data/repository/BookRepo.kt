package com.example.project.data.repository

import com.example.project.data.database.TaskDao
import com.example.project.data.model.Task
import kotlinx.coroutines.flow.Flow

class BookRepo(private val dao: TaskDao) {

    fun getAll(): Flow<List<Task>> {
        return dao.getAll()
    }

    fun getTaskById(id: Int): Task? {
        return dao.getTaskById(id)
    }

    fun getCompletedAll(): Flow<List<Task>> {
        return dao.getCompletedAll()
    }

    fun addTask(task: Task) {
        dao.addTask(task)
    }

    fun deleteTask(task: Task) {
        dao.deleteTask(task)
    }

    fun updateTask(task: Task) {
        dao.updateTask(task)
    }
}