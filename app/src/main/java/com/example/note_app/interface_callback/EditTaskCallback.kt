package com.example.note_app.interface_callback

import com.example.note_app.model.Task

interface EditTaskCallback {

    fun onItemButtonClick(item: Task)
}