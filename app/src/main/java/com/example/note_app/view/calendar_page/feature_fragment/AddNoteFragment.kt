package com.example.note_app.view.calendar_page.feature_fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.note_app.database.NoteDatabase
import com.example.note_app.databinding.FragmentAddNoteBinding
import com.example.note_app.note_interface_callback.UpdateNoteCallBack
import com.example.note_app.model.Note
import com.example.note_app.worker.MyNotificationWorker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


class AddNoteFragment(private val callback: UpdateNoteCallBack) : Fragment() {
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backToCalendar()
        receivedDataDate()
        addNote()
        setDateAndTime()

    }

    private fun receivedDataDate() {
        val dataDate = arguments?.getString("date").toString()
        binding.date.text = dataDate
    }

    private fun setDateAndTime() {
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

    private fun addNote() {
        binding.addNote.setOnClickListener {
            val textNote = binding.note.text
            if(textNote.isNullOrEmpty()) {
                Toast.makeText(context,"Note has not been entered yet",Toast.LENGTH_SHORT).show()
            }
            else {
                val note = binding.note.text.toString()
                val time = binding.time.text.toString()
                val date = binding.date.text.toString()
                val description = binding.description.text.toString()

                val newItemNote = Note(note = note, time = time, date = date, description = description)
                NoteDatabase.getDatabase(requireContext()).noteDAO().insert(newItemNote)

                val targetTime = "$time $date"
                val sdf = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
                val targetDate = sdf.parse(targetTime)
                if (targetDate != null) {
                    val currentTime = Calendar.getInstance().time
                    var delayMillis = targetDate.time - currentTime.time - 30*60*1000
                    delayMillis = maxOf(delayMillis,0)
                    if (delayMillis > 0) {
                        val workRequest = OneTimeWorkRequest.Builder(MyNotificationWorker::class.java)
                            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                            .build()
                        WorkManager.getInstance(requireContext()).enqueue(workRequest)
                    }
                }

                callback.updateNote()
                requireActivity().onBackPressed()
            }
        }
    }

    private fun backToCalendar() {
        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}