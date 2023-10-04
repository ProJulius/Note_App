package com.example.note_app.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.note_app.model.Note
import com.example.note_app.model.Task


@Dao
interface NoteDAO {
    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * from note WHERE date = :dataDate ORDER BY time ASC")
    fun getListNote(dataDate: String): List<Note>

    @Query("SELECT * from note")
    fun getListNoteToDate(): List<Note>
}