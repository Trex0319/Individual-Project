package com.example.project.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converter {

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? = value?.let {
        LocalDateTime.parse(it)
    }

    @TypeConverter
    fun dateTimestamp(dateTime: LocalDateTime?): String? = dateTime?.toString()
}