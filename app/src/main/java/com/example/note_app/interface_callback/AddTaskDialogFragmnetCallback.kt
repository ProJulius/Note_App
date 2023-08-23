package com.example.note_app.interface_callback

interface AddTaskDialogFragmnetCallback {
    fun onDataTaskReceived(dataTask: String, dataType: String, dataColor: String)
    fun onHidenCallback()
}