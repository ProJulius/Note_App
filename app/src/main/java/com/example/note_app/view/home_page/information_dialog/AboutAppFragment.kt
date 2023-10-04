package com.example.note_app.view.home_page.information_dialog

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.note_app.R
import com.example.note_app.databinding.FragmentAboutAppBinding
import com.example.note_app.databinding.FragmentTargetNoteDialogBinding
import com.example.note_app.task_interface_callback.DimerDialog

class AboutAppFragment(private val callback: DimerDialog) : DialogFragment() {
    private var _binding: FragmentAboutAppBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback.onHidenCallback()
    }
}