package com.example.note_app.view.home_page.information_dialog

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.note_app.R
import com.example.note_app.database.TaskDatabase
import com.example.note_app.databinding.FragmentHomeBinding
import com.example.note_app.databinding.FragmentTargetTaskDialogBinding
import com.example.note_app.task_interface_callback.AddTaskDialogFragmnetCallback
import com.example.note_app.task_interface_callback.DimerDialog

class TargetTaskDialogFragment(private val callback: DimerDialog) : DialogFragment() {
    private var _binding: FragmentTargetTaskDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTargetTaskDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showTargetTask()
    }

    @SuppressLint("SetTextI18n")
    private fun showTargetTask() {
        val itemTaskPersonal = TaskDatabase.getDatabase(requireContext()).taskDAO().getListPersonal()
        val itemTaskStudy = TaskDatabase.getDatabase(requireContext()).taskDAO().getListStudy()
        val itemTaskWork = TaskDatabase.getDatabase(requireContext()).taskDAO().getListWork()
        val itemTaskNone = TaskDatabase.getDatabase(requireContext()).taskDAO().getListNone()
        var countPersonal = 0
        var countStudy = 0
        var countWork = 0
        var countNone = 0

        for(it in itemTaskPersonal) {
            if(it.isCompleted) {
                countPersonal += 1
            }
        }
        for(it in itemTaskStudy) {
            if(it.isCompleted) {
                countStudy += 1
            }
        }
        for(it in itemTaskWork) {
            if(it.isCompleted) {
                countWork += 1
            }
        }
        for(it in itemTaskNone) {
            if(it.isCompleted) {
                countNone += 1
            }
        }

        binding.perOne.text = countPersonal.toString()
        binding.perTwo.text = '/' + itemTaskPersonal.size.toString()
        binding.stuOne.text = countStudy.toString()
        binding.stuTwo.text = '/' + itemTaskStudy.size.toString()
        binding.worOne.text = countWork.toString()
        binding.worTwo.text = '/' + itemTaskWork.size.toString()
        binding.nonOne.text = countNone.toString()
        binding.nonTwo.text = '/' + itemTaskNone.size.toString()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback.onHidenCallback()
    }
}