package com.example.note_app.task_interface_callback

interface AddTaskDialogFragmnetCallback {
    fun onDataTaskReceived(dataTask: String, dataType: String, dataColor: String, dataCheck: Boolean)
    fun onHidenCallback()
}