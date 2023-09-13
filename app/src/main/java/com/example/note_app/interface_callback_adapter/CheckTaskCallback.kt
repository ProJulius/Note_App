package com.example.note_app.interface_callback_adapter

import com.example.note_app.model.Task

interface CheckTaskCallback {
    fun onCheckTask(item: Task)
}