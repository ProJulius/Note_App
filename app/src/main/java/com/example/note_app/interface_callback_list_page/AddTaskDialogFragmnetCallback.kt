package com.example.note_app.interface_callback_list_page

interface AddTaskDialogFragmnetCallback {
    fun onDataTaskReceived(dataTask: String, dataType: String, dataColor: String, dataCheck: Boolean)
    fun onHidenCallback()
}