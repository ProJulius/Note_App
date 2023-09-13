package com.example.note_app.interface_callback_adapter

import com.example.note_app.model.Task

interface DeleteTaskCallback {
    fun onDeleteButtonClick(item: Task)

}