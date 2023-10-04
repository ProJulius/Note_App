package com.example.note_app.view.home_page.information_dialog

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.note_app.R
import com.example.note_app.adapter.NoteAdapter
import com.example.note_app.adapter.TargetNoteAdapter
import com.example.note_app.database.NoteDatabase
import com.example.note_app.databinding.FragmentTargetNoteDialogBinding
import com.example.note_app.databinding.FragmentTargetTaskDialogBinding
import com.example.note_app.task_interface_callback.DimerDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class TargetNoteDialogFragment(private val callback: DimerDialog) : DialogFragment() {
    private var _binding: FragmentTargetNoteDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTargetNoteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemList = NoteDatabase.getDatabase(requireContext()).noteDAO().getListNoteToDate()
        val listDateTmp = mutableSetOf<String>()
        val listDate = mutableListOf<String>()
        val listFinal = mutableListOf<String>()
        for(it in itemList) {
            listDateTmp.add(it.date + it.time)
        }
        for(it in listDateTmp) {
            val date = it.substring(0,2)
            val month = it.substring(3,5)
            val year = it.substring(6,10)
            val hour = it.substring(10,12)
            val minute = it.substring(13,15)
            val tmp = year + month + date + hour + minute
            listDate.add(tmp)
        }
        listDate.sort()
        for(it in listDate) {
            val date = it.substring(6,8)
            val month = it.substring(4,6)
            val year = it.substring(0,4)
            val hour = it.substring(10,12)
            val minute = it.substring(8,10)
            val tmp = minute + ':' + hour + " _ " + date + '/' + month + '/' + year
            listFinal.add(tmp)
        }

        if (listFinal.isEmpty()) {
            binding.imageList.visibility = View.VISIBLE
        }
        else {
            binding.imageList.visibility = View.GONE
        }
        val linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.layoutManager = linearLayoutManager
        val adapter = TargetNoteAdapter(listFinal)
        binding.recyclerView.adapter = adapter
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback.onHidenCallback()
    }
}