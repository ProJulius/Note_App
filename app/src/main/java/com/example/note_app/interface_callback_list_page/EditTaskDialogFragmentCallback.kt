package com.example.note_app.interface_callback_list_page

interface EditTaskDialogFragmentCallback {
    fun onDataTaskReceived(dataTask: String, dataType: String, dataColor: String)
    fun onHidenCallback()
}