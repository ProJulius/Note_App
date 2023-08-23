package com.example.note_app.view.list_page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.note_app.R
import com.example.note_app.constants.CONSTANTS
import com.example.note_app.databinding.FragmentListBinding
import com.example.note_app.interface_callback.AddTaskDialogFragmnetCallback
import com.example.note_app.view.bottom_sheet_dialog.AddTaskDialogFragment
import kotlin.math.abs


@Suppress("UNUSED_EXPRESSION")
class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var startX = 0f
    private var startY = 0f
    private var isMoving = false
    private var lastClickTime = 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moveButton()
    }

    private fun addNote() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= CONSTANTS.DEBOUNCE_DELAY) {
            lastClickTime = currentTime
            val addTaskDialogFragment = AddTaskDialogFragment(object : AddTaskDialogFragmnetCallback {
                override fun onDataTaskReceived(dataTask: String, dataType: String, dataColor: String) {

                }

                override fun onHidenCallback() {
                    hideDimmer()
                }
            })
            binding.dimmer.visibility = View.VISIBLE
            addTaskDialogFragment.show(parentFragmentManager, addTaskDialogFragment.tag)
        }
    }

    @SuppressLint("ClickableViewAccessibility", "CutPasteId", "ResourceType")
    private fun moveButton() {
        binding.buttonAdd.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                    // Tắt ViewPager2 khi di chuyển nút
                    activity?.findViewById<ViewPager2>(R.id.view_pager_2)?.isUserInputEnabled = false
                    isMoving = false
                }

                MotionEvent.ACTION_MOVE -> {
                    val endX = event.x
                    val endY = event.y
                    if (abs(endX) > CONSTANTS.LIMIT_BUTTON_MOVE || abs(endY) > CONSTANTS.LIMIT_BUTTON_MOVE) {
                        isMoving = true
                    }

                    // Lấy kích thước view pager
                    val fragmentContainer = activity?.findViewById<ViewPager2>(R.id.view_pager_2)
                    val fragmentWidth = fragmentContainer!!.width
                    val fragmentHeight = fragmentContainer.height

                    if (isMoving) {
                        var newX: Float = binding.buttonAdd.x + (endX - startX)
                        var newY: Float = binding.buttonAdd.y + (endY - startX)
                        newX = newX.coerceIn(0f, fragmentWidth - binding.buttonAdd.width.toFloat())
                        newY = newY.coerceIn(0f, fragmentHeight - binding.buttonAdd.height.toFloat())
                        binding.buttonAdd.x = newX
                        binding.buttonAdd.y = newY
                    }

                }

                MotionEvent.ACTION_UP -> {
                    // Bật lại ViewPager2 khi không di chuyển nút
                    activity?.findViewById<ViewPager2>(R.id.view_pager_2)?.isUserInputEnabled = true
                    if (!isMoving) {
                        addNote()
                    }
                }
            }
            true
        }
    }

    fun hideDimmer() {
        binding.dimmer.visibility = View.GONE
    }


}