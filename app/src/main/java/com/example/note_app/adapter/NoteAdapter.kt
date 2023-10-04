package com.example.note_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note_app.databinding.ItemNoteBinding
import com.example.note_app.note_interface_callback.NoteDeatailCallback
import com.example.note_app.model.Note

class NoteAdapter(
    private var listNote: List<Note>,
    private val noteDeatailCallback: NoteDeatailCallback) :  RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Note) {
            binding.note.text = item.note
            binding.time.text = item.time
            binding.date.text = item.date
            binding.description.text = item.description

            binding.itemNote.setOnClickListener {
                noteDeatailCallback.onItemButtonClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemNoteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listNote[position])
    }

}