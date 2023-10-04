package com.example.note_app.note_interface_callback

import com.example.note_app.model.Note

interface EditNoteCallback {
    fun editItemNote(item: Note, choose: String)
}