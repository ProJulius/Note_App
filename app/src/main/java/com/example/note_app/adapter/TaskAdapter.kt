package com.example.note_app.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.note_app.databinding.ItemTaskBinding
import com.example.note_app.model.Task
import com.example.note_app.R
import com.example.note_app.interface_callback_adapter.CheckTaskCallback
import com.example.note_app.interface_callback_adapter.DeleteTaskCallback
import com.example.note_app.interface_callback_adapter.EditTaskCallback
import java.util.Locale


class TaskAdapter(
    private var listTask: List<Task>,
    private val deleteTaskCallback: DeleteTaskCallback,
    private val editTaskCallback: EditTaskCallback,
    private val checkTaskCallback: CheckTaskCallback,
    ) : RecyclerView.Adapter<TaskAdapter.ViewHolder>(), Filterable {

    private var filteredTasks: List<Task> = listTask.toList()
    inner class ViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: Task) {
            binding.textTask.text = item.task
            binding.textType.text = item.type
            if(item.type.equals("None")) {
                binding.colorBox.setImageResource(R.drawable.icon_type_none)
            }
            else if(item.type.equals("Work")) {
                binding.colorBox.setImageResource(R.drawable.icon_type_work)
            }
            else if(item.type.equals("Study")) {
                binding.colorBox.setImageResource(R.drawable.icon_type_study)
            }
            else {
                binding.colorBox.setImageResource(R.drawable.icon_type_personal)
            }

            binding.checkBox.isChecked = item.isCompleted

            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                item.isCompleted = isChecked
                checkTaskCallback.onCheckTask(item)
                textCompleted(item)
            }

            binding.deleteTask.setOnClickListener {
                deleteTaskCallback.onDeleteButtonClick(item)
            }

            binding.mainLayout.setOnClickListener{
                editTaskCallback.onItemButtonClick(item)
            }

            textCompleted(item)
        }

        // Hiệu ứng gạch ngang chữ nếu đã hoàn thiện Task
        fun textCompleted(item: Task) {
            if(item.isCompleted) {
                binding.textTask.paintFlags =  Paint.STRIKE_THRU_TEXT_FLAG
            }
            else {
                binding.textTask.paintFlags = 0
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return filteredTasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredTasks[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val searchText = charSequence.toString().lowercase()
                val filteredList = ArrayList<Task>()


                if (searchText.isEmpty()) {
                    filteredList.addAll(listTask)
                } else {
                    for (item in listTask) {
                        if (item.task.lowercase().contains(searchText)) {
                            filteredList.add(item)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList

                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredTasks = filterResults.values as List<Task>
                notifyDataSetChanged()
            }
        }
    }

}