package com.example.note_app.view.bottom_sheet_dialog

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.note_app.R
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

        confirmNote()
        chooseType()

    }

    @SuppressLint("SetTextI18n")
    private fun chooseType() {
        binding.linearPersonal.setOnClickListener {
            binding.type.text = "Personal"
            binding.checkbox.setImageResource(R.drawable.icon_check_box_blue)
        }
        binding.linearStudy.setOnClickListener {
            binding.type.text = "Study"
            binding.checkbox.setImageResource(R.drawable.icon_check_box_green)
        }
        binding.linearWork.setOnClickListener {
            binding.type.text = "Work"
            binding.checkbox.setImageResource(R.drawable.icon_check_box_red)
        }
        binding.linearNone.setOnClickListener {
            binding.type.text = "None"
            binding.checkbox.setImageResource(R.drawable.icon_check_box_grey)
        }
    }

    private fun confirmNote() {
        binding.buttonConfirm.setOnClickListener {
            val dataText = binding.editTextTask.text.toString()
            val dataType = binding.type.text.toString()
            val dataColor : String = when (dataType) {
                "None" -> {
                    "Grey"
                }
                "Work" -> {
                    "Red"
                }
                "Study" -> {
                    "Green"
                }
                else -> {
                    "Blue"
                }
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