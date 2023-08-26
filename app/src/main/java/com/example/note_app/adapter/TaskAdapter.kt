package com.example.note_app.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.example.note_app.databinding.ItemTaskBinding
import com.example.note_app.model.Task
import com.chauthai.swipereveallayout.ViewBinderHelper

class TaskAdapter(private val listTask: List<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>(){
    class ViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(item: Task) {
            binding.textTask.text = item.task
            binding.textType.text = item.type
            binding.colorBox.setImageResource(item.color)
            binding.checkBox.isChecked = item.isCompleted
            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                // Handle checkbox state change
            }
        }

        override fun onClick(v: View?) {
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