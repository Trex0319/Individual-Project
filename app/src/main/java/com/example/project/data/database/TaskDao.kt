package com.example.project.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.project.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task WHERE status = 0 ORDER BY title DESC")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE status = 1 ORDER BY title DESC")
    fun getCompletedAll(): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE id = :id")
    fun getTaskById(id: Int): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(word: Task)

    @Query("UPDATE Task SET status = :status WHERE id = :id")
    fun updateTaskStatus(id: Int, status: Boolean)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(word: Task)

    @Delete
    fun deleteTask(word: Task)

    @Query("SELECT status FROM task WHERE id = :id")
    fun getTaskStatus(id: Int): Boolean
}