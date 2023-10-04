package com.example.note_app.task_interface_callback

interface EditTaskDialogFragmentCallback {
    fun onDataTaskReceived(dataTask: String, dataType: String, dataColor: String)
    fun onHidenCallback()
}