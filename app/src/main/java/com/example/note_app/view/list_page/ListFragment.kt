package com.example.note_app.view.list_page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.note_app.R
import com.example.note_app.adapter.TaskAdapter
import com.example.note_app.constants.CONSTANTS
import com.example.note_app.database.TaskDatabase
import com.example.note_app.databinding.FragmentListBinding
import com.example.note_app.interface_callback.AddTaskDialogFragmnetCallback
import com.example.note_app.interface_callback.DeleteTaskCallback
import com.example.note_app.interface_callback.DeleteTaskDialogFragmentCallback
import com.example.note_app.interface_callback.EditTaskCallback
import com.example.note_app.interface_callback.EditTaskDialogFragmentCallback
import com.example.note_app.model.Task
import com.example.note_app.view.list_page.feature.AddTaskDialogFragment
import com.example.note_app.view.list_page.feature.DeleteTaskDialogFragment
import com.example.note_app.view.list_page.feature.EditTaskDilalogFragment
import kotlin.math.abs


@Suppress("UNUSED_EXPRESSION")
class ListFragment : Fragment(), DeleteTaskCallback, EditTaskCallback{
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
        manageRecyclerView()
    }

    // Khởi tạo recycler view
    private fun manageRecyclerView() {
        val itemList = TaskDatabase.getDatabase(requireContext()).taskDAO().getListTask()
        val linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.layoutManager = linearLayoutManager

        val adapter = TaskAdapter(itemList, this, this)
        binding.recyclerView.adapter = adapter
    }

    // Xử lí logic cho nút add
    @SuppressLint("ClickableViewAccessibility", "CutPasteId")
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
                    // Giới hạn trong màn hình
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

    // Thêm một task
    private fun addNote() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= CONSTANTS.DEBOUNCE_DELAY) {
            lastClickTime = currentTime
            val addTaskDialogFragment = AddTaskDialogFragment(object : AddTaskDialogFragmnetCallback {
                override fun onDataTaskReceived(
                    dataTask: String,
                    dataType: String,
                    dataColor: String,
                    dataCheck: Boolean
                ) {
                    val newTask = Task(task = dataTask, type = dataType, color = dataColor, isCompleted = dataCheck)
                    TaskDatabase.getDatabase(requireContext()).taskDAO().insert(newTask)
                    updateData()
                }

                override fun onHidenCallback() {
                    hideDimmer()
                }
            })
            binding.dimmer.visibility = View.VISIBLE
            addTaskDialogFragment.show(parentFragmentManager, addTaskDialogFragment.tag)
        }
    }

    // Xử lí xự kiện khi click vào thùng rác
    override fun onDeleteButtonClick(item: Task) {
        val deleteTaskDialogFragment = DeleteTaskDialogFragment(object : DeleteTaskDialogFragmentCallback{
            override fun confirmDelete(check: Boolean) {
                if(check) {
                    TaskDatabase.getDatabase(requireContext()).taskDAO().delete(item)
                    updateData()
                }
            }
            override fun onHidenCallback() {
                hideDimmer()
            }
        }, R.style.YourCustomDialogFragmentStyle)

        deleteTaskDialogFragment.setStyle(R.drawable.background_dialog, R.style.YourCustomDialogFragmentStyle)
        binding.dimmer.visibility = View.VISIBLE
        deleteTaskDialogFragment.show(parentFragmentManager, deleteTaskDialogFragment.tag)
    }

    // Xử lí sự kiện khi click vào 1 item
    override fun onItemButtonClick(item: Task) {
        val deleteTaskDialogFragment = EditTaskDilalogFragment(object :EditTaskDialogFragmentCallback{
            override fun onDataTaskReceived(
                dataTask: String,
                dataType: String,
                dataColor: String,
            ) {
                item.task = dataTask
                item.type = dataType
                item.color = dataColor
                TaskDatabase.getDatabase(requireContext()).taskDAO().update(item)
                updateData()
            }

            override fun onHidenCallback() {
                hideDimmer()
            }
        })
        binding.dimmer.visibility = View.VISIBLE
        deleteTaskDialogFragment.show(parentFragmentManager, deleteTaskDialogFragment.tag)
    }

    // Cập nhật recycler view sau khi cập nhật room
    fun updateData() {
        binding.recyclerView.adapter = TaskAdapter(TaskDatabase.getDatabase(requireContext()).taskDAO().getListTask(), this, this)
    }

    // Bật xám màn hình khi mở dialog
    fun hideDimmer() {
        binding.dimmer.visibility = View.GONE
    }
}