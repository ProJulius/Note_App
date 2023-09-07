package com.example.note_app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.example.note_app.databinding.ItemTaskBinding
import com.example.note_app.model.Task
import com.example.note_app.R
import com.example.note_app.database.TaskDatabase
import com.example.note_app.interface_callback.DeleteTaskCallback
import com.example.note_app.interface_callback.EditTaskCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class TaskAdapter(
    private val listTask: List<Task>,
    private val deleteTaskCallback: DeleteTaskCallback,
    private val editTaskCallback: EditTaskCallback,
    ) : RecyclerView.Adapter<TaskAdapter.ViewHolder>(){
    inner class ViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){
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

                CoroutineScope(Dispatchers.IO).launch {
                    TaskDatabase.getDatabase(binding.root.context).taskDAO().update(item)
                }
            }

            binding.deleteTask.setOnClickListener {
                deleteTaskCallback.onDeleteButtonClick(item)
            }

            binding.mainLayout.setOnClickListener{
                editTaskCallback.onItemButtonClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return listTask.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listTask[position])
    }

}