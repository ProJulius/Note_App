package com.example.note_app.task_interface_callback

import com.example.note_app.model.Task

interface CheckTaskCallback {
    fun onCheckTask(item: Task)
}