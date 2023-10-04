package com.example.note_app.view.calendar_page

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.note_app.R
import com.example.note_app.adapter.NoteAdapter
import com.example.note_app.constants.CONSTANTS
import com.example.note_app.database.NoteDatabase
import com.example.note_app.databinding.FragmentCalendarBinding
import com.example.note_app.note_interface_callback.NoteDeatailCallback
import com.example.note_app.note_interface_callback.UpdateNoteCallBack
import com.example.note_app.model.Note
import com.example.note_app.note_interface_callback.EditNoteCallback
import com.example.note_app.view.calendar_page.feature_fragment.AddNoteFragment
import com.example.note_app.view.calendar_page.feature_fragment.NoteDetailFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment()
                        , NoteDeatailCallback
{
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpDate()
        setUpRecyclerView()
        addNoteByDate()
        chooseDate()
        clearNote()
    }

    private fun clearNote() {
        val todayDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        val formattedDate = dateFormat.format(todayDate)

        val listNote = NoteDatabase.getDatabase(requireContext()).noteDAO().getListNoteToDate()
        val listDateTmp = mutableSetOf<String>()
        for(it in listNote) {
            listDateTmp.add(it.date)
            val date = it.date.substring(0,2)
            val month = it.date.substring(3,5)
            val year = it.date.substring(6,10)
            val hour = it.time.substring(3,5)
            val minute = it.time.substring(0,2)
            val tmp = year + '/' + month + '/' + date + ' ' + hour + ':' + minute
            if (tmp < formattedDate) {
                NoteDatabase.getDatabase(requireContext()).noteDAO().delete(it)
                setUpRecyclerView()
            }
        }
    }

    private fun setUpDate() {
        val todayDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(todayDate)
        binding.date.text = formattedDate

        binding.recyclerView.adapter = NoteAdapter(
            NoteDatabase
                .getDatabase(requireContext())
                .noteDAO()
                .getListNote(binding.date.text.toString()), this@CalendarFragment)
    }

    private fun chooseDate() {
        binding.date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = String.format("%02d/%02d/%02d", selectedDay, selectedMonth + 1, selectedYear)
                    binding.date.text = selectedDate
                    binding.recyclerView.adapter = NoteAdapter(
                        NoteDatabase
                            .getDatabase(requireContext())
                            .noteDAO()
                            .getListNote(binding.date.text.toString()), this@CalendarFragment)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    private fun setUpRecyclerView() {
        val itemList = NoteDatabase.getDatabase(requireContext()).noteDAO().getListNote(binding.date.text.toString())
        val linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.recyclerView.layoutManager = linearLayoutManager
        val adapter = NoteAdapter(itemList, this)
        binding.recyclerView.adapter = adapter
    }

    private fun addNoteByDate() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            val date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)

            val bundle = Bundle()
            val addNoteFragment = AddNoteFragment(object : UpdateNoteCallBack {
                override fun updateNote() {
                    binding.recyclerView.adapter = NoteAdapter(
                        NoteDatabase
                            .getDatabase(requireContext())
                            .noteDAO()
                            .getListNote(binding.date.text.toString()), this@CalendarFragment)
                }
            })
            bundle.putString("date", date)
            addNoteFragment.arguments = bundle

            if (requireFragmentManager().findFragmentByTag("AddNoteFragment") == null){
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(binding.fragmentCalendar.id, addNoteFragment, "AddNoteFragment")
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onItemButtonClick(item: Note) {
        val noteDeatailFragment = NoteDetailFragment(object :EditNoteCallback{
            override fun editItemNote(item: Note, choose: String) {
                if(choose.equals("edit")) {
                    NoteDatabase.getDatabase(requireContext()).noteDAO().update(item)
                    binding.recyclerView.adapter = NoteAdapter(
                        NoteDatabase
                            .getDatabase(requireContext())
                            .noteDAO()
                            .getListNote(binding.date.text.toString()), this@CalendarFragment)
                }
                else {
                    NoteDatabase.getDatabase(requireContext()).noteDAO().delete(item)
                    binding.recyclerView.adapter = NoteAdapter(
                        NoteDatabase
                            .getDatabase(requireContext())
                            .noteDAO()
                            .getListNote(binding.date.text.toString()), this@CalendarFragment)
                }
            }

        })
        val bundle = Bundle()
        val dataID = item.id
        val dataNote = item.note
        val dataTime = item.time
        val dataDate = item.date
        val dataDesCription = item.description
        bundle.putInt("id", dataID)
        bundle.putString("note", dataNote)
        bundle.putString("time", dataTime)
        bundle.putString("date", dataDate)
        bundle.putString("description", dataDesCription)
        noteDeatailFragment.arguments = bundle
        if (requireFragmentManager().findFragmentByTag("NoteDeatailFragment") == null){
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentCalendar.id, noteDeatailFragment, "NoteDeatailFragment")
                .addToBackStack(null)
                .commit()
        }

    }
}