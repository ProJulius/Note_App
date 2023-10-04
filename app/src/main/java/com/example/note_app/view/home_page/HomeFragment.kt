package com.example.note_app.view.home_page

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.note_app.R
import com.example.note_app.database.TaskDatabase
import com.example.note_app.databinding.FragmentCalendarBinding
import com.example.note_app.databinding.FragmentHomeBinding
import com.example.note_app.model.Task
import com.example.note_app.task_interface_callback.AddTaskDialogFragmnetCallback
import com.example.note_app.task_interface_callback.DimerDialog
import com.example.note_app.view.home_page.information_dialog.AboutAppFragment
import com.example.note_app.view.home_page.information_dialog.TargetNoteDialogFragment
import com.example.note_app.view.home_page.information_dialog.TargetTaskDialogFragment
import com.example.note_app.view.list_page.feature_dialog.AddTaskDialogFragment
import com.example.note_app.worker.MyNotificationWorker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        targetNote()
        targetTask()
        setting()
        aboutApp()

    }

    private fun aboutApp() {
        binding.about.setOnClickListener {
            val aboutAppFragment = AboutAppFragment(object :DimerDialog{
                override fun onHidenCallback() {
                    hideDimmer()
                }
            })
            displayDimmer()
            aboutAppFragment.setStyle(R.drawable.background_dialog, R.style.YourCustomDialogFragmentStyle)
            aboutAppFragment.show(parentFragmentManager, aboutAppFragment.tag)
        }
    }

    private fun setting() {
        binding.setting.setOnClickListener {

        }
    }

    private fun targetTask() {
        binding.targetTask.setOnClickListener {
            val targetTaskDialogFragment = TargetTaskDialogFragment(object :DimerDialog{
                override fun onHidenCallback() {
                    hideDimmer()
                }
            })
            displayDimmer()
            targetTaskDialogFragment.setStyle(R.drawable.background_dialog, R.style.YourCustomDialogFragmentStyle)
            targetTaskDialogFragment.show(parentFragmentManager, targetTaskDialogFragment.tag)
        }
    }

    private fun targetNote() {
        binding.targetNote.setOnClickListener {
            val targetNoteDialogFragment = TargetNoteDialogFragment(object :DimerDialog{
                override fun onHidenCallback() {
                    hideDimmer()
                }

            })
            displayDimmer()
            targetNoteDialogFragment.setStyle(R.drawable.background_dialog, R.style.YourCustomDialogFragmentStyle)
            targetNoteDialogFragment.show(parentFragmentManager, targetNoteDialogFragment.tag)
        }
    }

    fun hideDimmer() {
        binding.dimmer.visibility = View.GONE
    }
    fun displayDimmer() {
        binding.dimmer.visibility = View.VISIBLE
    }

        private fun calculateTimeDiff(dateTimeString: String, format: String): Long {
            val targetTime = convertStringToDateTime(dateTimeString, format).time
            val currentTime = Calendar.getInstance().time.time
            return targetTime - currentTime
        }

        private fun convertStringToDateTime(dateTimeString: String, format: String): Date {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            return sdf.parse(dateTimeString) ?: Date()
        }
}