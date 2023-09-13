package com.example.note_app.interface_callback_adapter

import com.example.note_app.model.Task

interface EditTaskCallback {

    fun onItemButtonClick(item: Task)

}