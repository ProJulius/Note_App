package com.example.note_app.view.bottom_sheet_dialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.note_app.databinding.FragmentAddTaskDialogBinding
import com.example.note_app.interface_callback.AddTaskDialogFragmnetCallback
import com.example.note_app.view.list_page.ListFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddTaskDialogFragment(val callback: AddTaskDialogFragmnetCallback) : BottomSheetDialogFragment() {
    private var _binding: FragmentAddTaskDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonConfirm.setOnClickListener {
            val dataText = binding.editTextTask.text.toString()
            val dataType = binding.type.text.toString()
            val dataColor : String
            if(dataType.equals("None")) {
                dataColor = "Grey"
            }
            else if(dataType.equals("Work")) {
                dataColor = "Red"
            }
            else if(dataType.equals("Study")) {
                dataColor = "Green"
            }
            else {
                dataColor = "Blue"
            }

            callback.onDataTaskReceived(dataText, dataType, dataColor)
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback.onHidenCallback()
    }
}