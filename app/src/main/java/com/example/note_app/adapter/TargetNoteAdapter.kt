package com.example.note_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note_app.databinding.ItemNoteBinding
import com.example.note_app.databinding.ItemTargetNoteBinding
import com.example.note_app.model.Note

class TargetNoteAdapter(private var listNote: List<String>,):  RecyclerView.Adapter<TargetNoteAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemTargetNoteBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: String) {
            binding.date.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTargetNoteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listNote[position])
    }

}