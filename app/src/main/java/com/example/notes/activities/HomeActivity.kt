package com.example.notes.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.databinding.ActivityHomeBinding
import com.example.notes.models.AllObjects.UID
import com.example.notes.models.AllObjects.database
import com.example.notes.models.NotesData
import com.example.notes.recycler.NotesAdapter
import com.example.notes.recycler.SwipeToDeleteCallback
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var list: ArrayList<NotesData> = arrayListOf()
    private var getAllNotesJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UID = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        setViews()
        setRecycler()
        CoroutineScope(Dispatchers.IO).launch {
            getAllNotes()
        }
        clickListeners()
    }

    private fun getAllNotes() {
        getAllNotesJob?.cancel()
        getAllNotesJob =  CoroutineScope(Dispatchers.IO).launch {
            database.child(UID).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        list.clear()
                        for (noteSnapshot in snapshot.children) {
                            val note = noteSnapshot.getValue(NotesData::class.java)
                            list.add(note!!)
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.rvAll.adapter?.notifyDataSetChanged()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
        }
    }
    private fun setViews() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    private fun clickListeners() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
        binding.bDelete.setOnClickListener {
            if(list.isEmpty()){
                Snackbar.make(it, "No Notes Found!", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            showDialogBox()
        }
    }
    private fun showDialogBox() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to delete all notes?")

        builder.setPositiveButton("Yes") { _, _ ->
            GlobalScope.launch {
                database.child(UID).removeValue()
            }
            list.clear()
            binding.rvAll.adapter?.notifyDataSetChanged()
            Snackbar.make(findViewById(android.R.id.content),
                "All notes Deleted!",
                Snackbar.LENGTH_LONG).show()

        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
    private fun setRecycler() {
        binding.rvAll.layoutManager = LinearLayoutManager(this)
        binding.rvAll.adapter = NotesAdapter(this, list)
        val swipeToDeleteCallback = SwipeToDeleteCallback(NotesAdapter(this, list), list)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvAll)
    }
}



