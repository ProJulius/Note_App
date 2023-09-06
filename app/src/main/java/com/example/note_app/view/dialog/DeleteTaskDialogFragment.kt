package com.example.note_app.view.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.note_app.R
import com.example.note_app.databinding.FragmentDeleteTaskDialogBinding
import com.example.note_app.interface_callback.DeleteTaskDialogCallback


class DeleteTaskDialogFragment(
    private val callback: DeleteTaskDialogCallback,
    yourCustomDialogFragmentStyle: Int
) : DialogFragment() {
    private var _binding: FragmentDeleteTaskDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteTaskDialogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.delete.setOnClickListener {
            val oke = true

            callback.confirmDelete(oke)
            dismiss()
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback.onHidenCallback()
    }
}