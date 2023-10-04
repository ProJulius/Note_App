package com.example.note_app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.note_app.model.Task

@Dao
interface TaskDAO {
    @Insert
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * from task ORDER BY isCompleted ASC")
    fun getListTask(): List<Task>

    @Query("SELECT * from task WHERE type = 'Personal' ORDER BY isCompleted ASC")
    fun getListPersonal(): List<Task>

    @Query("SELECT * from task WHERE type = 'Study' ORDER BY isCompleted ASC")
    fun getListStudy(): List<Task>

    @Query("SELECT * from task WHERE type = 'Work' ORDER BY isCompleted ASC")
    fun getListWork(): List<Task>

    @Query("SELECT * from task WHERE type = 'None' ORDER BY isCompleted ASC")
    fun getListNone(): List<Task>


}