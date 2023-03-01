package com.example.notes.recycler

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.models.AllObjects
import com.example.notes.models.NotesData
import com.example.notes.R
import com.example.notes.activities.AddNoteActivity

class NotesAdapter(private val context: Context, private val notes: ArrayList<NotesData>) :
    RecyclerView.Adapter<NotesAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(context).inflate(R.layout.item_notes, parent, false))
    }
    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.tvTitle.text = notes[position].title
        holder.tvDesc.text = notes[position].desc
        holder.tvDate.text = notes[position].dateTime

        holder.card.setOnClickListener {
            AllObjects.notesData = notes[position]
            context.startActivity(Intent(context, AddNoteActivity::class.java))
        }
        holder.card.setOnLongClickListener {

            true
        }
    }
    override fun getItemCount(): Int {
        return notes.size
    }
    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.card)
        val tvTitle:TextView = itemView.findViewById(R.id.tvTitles)
        val tvDesc:TextView = itemView.findViewById(R.id.tvDesc)
        val tvDate:TextView = itemView.findViewById(R.id.tvDateTime)

    }
}