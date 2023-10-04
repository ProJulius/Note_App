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
import com.example.note_app.task_interface_callback.CheckTaskCallback
import com.example.note_app.task_interface_callback.DeleteTaskCallback
import com.example.note_app.task_interface_callback.EditTaskCallback
import com.example.note_app.task_interface_callback.AddTaskDialogFragmnetCallback
import com.example.note_app.task_interface_callback.DeleteTaskDialogFragmentCallback
import com.example.note_app.task_interface_callback.EditTaskDialogFragmentCallback
import com.example.note_app.model.Task
import com.example.note_app.view.list_page.feature_dialog.AddTaskDialogFragment
import com.example.note_app.view.list_page.feature_dialog.DeleteTaskDialogFragment
import com.example.note_app.view.list_page.feature_dialog.EditTaskDilalogFragment
import kotlin.math.abs


class ListFragment : Fragment()
                    , DeleteTaskCallback
                    , EditTaskCallback
                    , CheckTaskCallback
{
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var startX = 0f
    private var startY = 0f
    private var isMoving = false
    private var lastClickAddTime = 0L
    private var lastClickItemTime = 0L
    private var checkSort = false
    private var checkboxSort = "khong"
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
        createSortDialog()
        filterTask()
        choosefilterTask()
        searchItem()
    }

    private fun searchItem() {
        binding.search.setOnClickListener {
            val search_text = binding.searchTxt.text.trim()
            val itemList = TaskDatabase.getDatabase(requireContext()).taskDAO().getListTask()
            if(search_text.isEmpty()) {
                binding.recyclerView.adapter = TaskAdapter(
                    TaskDatabase
                        .getDatabase(requireContext())
                        .taskDAO()
                        .getListTask(), this, this, this)
            }
            else {
                val itemFilter = mutableListOf<Task>()
                for(it in itemList) {
                    if(it.task.contains(search_text)) {
                        itemFilter.add(it)
                    }
                }
                binding.recyclerView.adapter = TaskAdapter(
                    itemFilter, this, this, this)
            }
        }
    }

    // Khởi tạo dialog sắp xếp ở dạng ẩn
    private fun createSortDialog(){
        binding.chooseSort.visibility = View.GONE
    }

    // Mở dialog sắp xếp
    private fun filterTask() {
        binding.sort.setOnClickListener {
            if (!checkSort) {
                binding.chooseSort.visibility = View.VISIBLE
                binding.sort.setImageResource(R.drawable.icon_sort_right)
                displayDimmer()
                checkSort = true
            }
            else {
                binding.chooseSort.visibility = View.GONE
                binding.sort.setImageResource(R.drawable.icon_sort)
                hideDimmer()
                checkSort = false
            }
        }
    }

    // Chọn dạng sắp xếp
    private fun choosefilterTask() {
        binding.personal.setOnClickListener {
            updateDataPersonal()
            binding.chooseSort.visibility = View.GONE
            binding.sort.setImageResource(R.drawable.icon_sort)
            hideDimmer()
            checkSort = false
            checkboxSort = "personal"
        }
        binding.study.setOnClickListener {
            updateDataStudy()
            binding.chooseSort.visibility = View.GONE
            binding.sort.setImageResource(R.drawable.icon_sort)
            hideDimmer()
            checkSort = false
            checkboxSort = "study"
        }
        binding.work.setOnClickListener {
            updateDataWork()
            binding.chooseSort.visibility = View.GONE
            binding.sort.setImageResource(R.drawable.icon_sort)
            hideDimmer()
            checkSort = false
            checkboxSort = "work"
        }
        binding.none.setOnClickListener {
            updateDataNone()
            binding.chooseSort.visibility = View.GONE
            binding.sort.setImageResource(R.drawable.icon_sort)
            hideDimmer()
            checkSort = false
            checkboxSort = "none"
        }
        binding.khong.setOnClickListener {
            updateData()
            binding.chooseSort.visibility = View.GONE
            binding.sort.setImageResource(R.drawable.icon_sort)
            hideDimmer()
            checkSort = false
            checkboxSort = "khong"
        }
    }

    // Khởi tạo recycler view
    private fun manageRecyclerView() {
        val itemList = TaskDatabase.getDatabase(requireContext()).taskDAO().getListTask()
        val linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.layoutManager = linearLayoutManager

        val adapter = TaskAdapter(itemList, this, this, this)
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
                    turnOffViewPager()
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
                    turnOnViewPager()
                    if (!isMoving) {
                        addNote()
                    }
                }
            }
            true
        }
    }

    // Thêm một task
    @SuppressLint("NotifyDataSetChanged")
    private fun addNote() {
        val currentAddTime = System.currentTimeMillis()
        if (currentAddTime - lastClickAddTime >= CONSTANTS.DEBOUNCE_DELAY) {
            lastClickAddTime = currentAddTime
            val addTaskDialogFragment = AddTaskDialogFragment(object :
                AddTaskDialogFragmnetCallback {
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
        val deleteTaskDialogFragment = DeleteTaskDialogFragment(object :
            DeleteTaskDialogFragmentCallback {
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
        val currentItemTime = System.currentTimeMillis()
        if (currentItemTime - lastClickItemTime >= CONSTANTS.DEBOUNCE_DELAY) {
            lastClickItemTime = currentItemTime
            val deleteTaskDialogFragment =
                EditTaskDilalogFragment(object : EditTaskDialogFragmentCallback {
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
                }, item.task, item.type)
            binding.dimmer.visibility = View.VISIBLE
            deleteTaskDialogFragment.show(parentFragmentManager, deleteTaskDialogFragment.tag)
        }
    }

    // Bật ViewPager
    fun turnOnViewPager() {
        activity?.findViewById<ViewPager2>(R.id.view_pager_2)?.isUserInputEnabled = true
    }

    // Tắt ViewPager
    fun turnOffViewPager() {
        activity?.findViewById<ViewPager2>(R.id.view_pager_2)?.isUserInputEnabled = false
    }

    // Bật xám màn hình khi mở dialog
    fun hideDimmer() {
        binding.dimmer.visibility = View.GONE
    }

    // Tắt xám màn hình khi mở dialog
    fun displayDimmer() {
        binding.dimmer.visibility = View.VISIBLE
    }

    // Update danh sách khi click vào checkbox
    override fun onCheckTask(item: Task) {
        TaskDatabase.getDatabase(binding.root.context).taskDAO().update(item)
        if(checkboxSort == "personal") {
            updateDataPersonal()
        }
        else if(checkboxSort == "study") {
            updateDataStudy()
        }
        else if(checkboxSort == "work") {
            updateDataWork()
        }
        else if(checkboxSort == "none") {
            updateDataNone()
        }
        else {
            updateData()
        }
    }

    // Cập nhật recycler view sau khi cập nhật room
    fun updateData() {
        binding.recyclerView.adapter = TaskAdapter(
            TaskDatabase
                .getDatabase(requireContext())
                .taskDAO()
                .getListTask(), this, this, this)
    }

    private fun updateDataNone() {
        binding.recyclerView.adapter = TaskAdapter(
            TaskDatabase
                .getDatabase(requireContext())
                .taskDAO()
                .getListNone(), this, this, this)
    }

    private fun updateDataWork() {
        binding.recyclerView.adapter = TaskAdapter(
            TaskDatabase
                .getDatabase(requireContext())
                .taskDAO()
                .getListWork(), this, this, this)
    }

    private fun updateDataStudy() {
        binding.recyclerView.adapter = TaskAdapter(
            TaskDatabase
                .getDatabase(requireContext())
                .taskDAO()
                .getListStudy(), this, this, this)
    }

    private fun updateDataPersonal() {
        binding.recyclerView.adapter = TaskAdapter(
            TaskDatabase
                .getDatabase(requireContext())
                .taskDAO()
                .getListPersonal(), this, this, this)
    }

}