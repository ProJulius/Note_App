package com.example.note_app.note_interface_callback

import com.example.note_app.model.Note

interface NoteDeatailCallback {
    fun onItemButtonClick(item: Note)
}