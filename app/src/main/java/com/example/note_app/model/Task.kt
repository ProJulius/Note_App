package com.example.note_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var task: String,
    var type: String,
    var color: String,
    var isCompleted: Boolean,
)
