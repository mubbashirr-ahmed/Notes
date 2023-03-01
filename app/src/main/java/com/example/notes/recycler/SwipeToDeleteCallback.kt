package com.example.notes.recycler

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.models.AllObjects
import com.example.notes.models.AllObjects.UID
import com.example.notes.models.NotesData
import com.google.android.material.snackbar.Snackbar

class SwipeToDeleteCallback(
    private val adapter: RecyclerView.Adapter<*>,
    private val notes: ArrayList<NotesData>
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (position != RecyclerView.NO_POSITION && notes.isNotEmpty() && position < notes.size) {
            try {
                val notesCopy = ArrayList(notes)
                notesCopy.removeAt(position)
                adapter.notifyItemRemoved(position)
                AllObjects.database.child(UID).child(notes[position].timeStamp.toString())
                    .removeValue()
                Snackbar.make(viewHolder.itemView, "Note Deleted", Snackbar.LENGTH_LONG).show()
            }
            catch (e:Exception){
                Log.i("errrr", e.message.toString())
            }

        }

    }

}
