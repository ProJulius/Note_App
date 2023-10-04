package com.example.note_app.view.calendar_page.feature_fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.note_app.databinding.FragmentNoteDetailBinding
import com.example.note_app.model.Note
import com.example.note_app.note_interface_callback.EditNoteCallback
import com.example.note_app.view.calendar_page.CalendarFragment
import java.util.Calendar

class NoteDetailFragment(private val callback: EditNoteCallback) : Fragment() {
    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receivedData()
        setDateandTime()
        editNote()
        deleteNote()
        backToCalendar()
    }

    private fun backToCalendar() {
        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun deleteNote() {
        binding.delete.setOnClickListener {
            val id = arguments?.getInt("id")!!.toInt()
            val note = binding.note.text.toString()
            val time = binding.time.text.toString()
            val date = binding.date.text.toString()
            val description = binding.description.text.toString()
            val newNote =
                Note(id = id, note = note, time = time, date = date, description = description)
            callback.editItemNote(newNote,"delete")
            requireActivity().onBackPressed()
        }
    }

    private fun editNote() {
        binding.edit.setOnClickListener {
            val id = arguments?.getInt("id")!!.toInt()
            val note = binding.note.text.toString()
            val time = binding.time.text.toString()
            val date = binding.date.text.toString()
            val description = binding.description.text.toString()
            if(note.isNullOrEmpty()) {
                Toast.makeText(context,"Note has not been entered yet", Toast.LENGTH_SHORT).show()
            }
            else {
                val newNote =
                    Note(id = id, note = note, time = time, date = date, description = description)
                callback.editItemNote(newNote,"edit")
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setDateandTime() {
        binding.buttonDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = String.format("%02d/%02d/%02d", selectedDay, selectedMonth + 1, selectedYear)
                    binding.date.text = selectedDate
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }

        binding.buttonTime.setOnClickListener(View.OnClickListener {
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    binding.time.text = selectedTime
                },
                currentHour,
                currentMinute,
                true
            )

            timePickerDialog.show()
        })
    }

    private fun receivedData() {
        val dataNote = arguments?.getString("note").toString()
        val dataTime = arguments?.getString("time").toString()
        val dataDate = arguments?.getString("date").toString()
        val dataDescription = arguments?.getString("description").toString()

        binding.note.setText(dataNote)
        binding.time.text = dataTime
        binding.date.text = dataDate
        binding.description.setText(dataDescription)
    }

}