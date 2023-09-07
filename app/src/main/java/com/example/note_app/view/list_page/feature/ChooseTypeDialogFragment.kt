package com.example.note_app.view.list_page.feature

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.note_app.databinding.FragmentChooseTypeDialogBinding
import com.example.note_app.interface_callback.ChooseTypeDialogFragmentCallback

class ChooseTypeDialogFragment(
    private val callback: ChooseTypeDialogFragmentCallback,
    yourCustomDialogFragmentStyle: Int
) : DialogFragment() {
    private var _binding: FragmentChooseTypeDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseTypeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.linear1.setOnClickListener {
            callback.chooseType("Personal")
            dismiss()
        }
        binding.linear2.setOnClickListener {
            callback.chooseType("Study")
            dismiss()
        }
        binding.linear3.setOnClickListener {
            callback.chooseType("Work")
            dismiss()
        }
        binding.linear4.setOnClickListener {
            callback.chooseType("None")
            dismiss()
        }
    }

}