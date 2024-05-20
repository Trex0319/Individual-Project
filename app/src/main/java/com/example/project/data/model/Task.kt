package com.example.project.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val mean: String,
    val syn: String,
    val detail: String,
    val date: LocalDateTime = LocalDateTime.now(),
    val status: Boolean? = false,
    val priority: Byte = 0
)