package com.example.note_app.view.list_page.feature_dialog

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.note_app.R
import com.example.note_app.databinding.FragmentEditTaskDilalogBinding
import com.example.note_app.interface_callback_list_page.ChooseTypeDialogFragmentCallback
import com.example.note_app.interface_callback_list_page.EditTaskDialogFragmentCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditTaskDilalogFragment(
    private val callback: EditTaskDialogFragmentCallback,
    private val textEdit: String,
    private val typeEdit: String,
) : BottomSheetDialogFragment() {
    private var _binding: FragmentEditTaskDilalogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTaskDilalogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataReceived()
        binding.save.setOnClickListener {
            val dataTask = binding.taskInputLayout.text.toString()
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
            callback.onDataTaskReceived(dataTask, dataType, dataColor)
            dismiss()
        }

        // Choose type of item
        binding.buttonChoose.setOnClickListener {
            val chooseTypeDialogFragment = ChooseTypeDialogFragment(object :
                ChooseTypeDialogFragmentCallback {
                override fun chooseType(type: String) {
                    binding.type.text = type
                    if (type == "Personal") {
                        binding.checkbox.setImageResource(R.drawable.icon_type_personal)
                    }
                    else if(type == "Study") {
                        binding.checkbox.setImageResource(R.drawable.icon_type_study)
                    }
                    else if(type == "Work") {
                        binding.checkbox.setImageResource(R.drawable.icon_type_work)
                    }
                    else {
                        binding.checkbox.setImageResource(R.drawable.icon_type_none)
                    }
                }

            },  R.style.YourCustomDialogFragmentStyle)
            binding.dimmer.visibility = View.VISIBLE
            chooseTypeDialogFragment.setStyle(R.drawable.background_dialog, R.style.YourCustomDialogFragmentStyle)
            chooseTypeDialogFragment.show(parentFragmentManager, chooseTypeDialogFragment.tag)
        }
    }

    // Truyền dữ liệu vào Edit text
    private fun setupDataReceived() {
        binding.taskInputLayout.setText(textEdit)
        binding.type.text = typeEdit
        if (typeEdit == "Personal") {
            binding.checkbox.setImageResource(R.drawable.icon_type_personal)
        }
        else if(typeEdit == "Study") {
            binding.checkbox.setImageResource(R.drawable.icon_type_study)
        }
        else if(typeEdit == "Work") {
            binding.checkbox.setImageResource(R.drawable.icon_type_work)
        }
        else {
            binding.checkbox.setImageResource(R.drawable.icon_type_none)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback.onHidenCallback()
    }

}