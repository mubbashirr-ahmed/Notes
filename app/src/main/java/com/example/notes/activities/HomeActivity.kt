package com.example.notes.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.R
import com.example.notes.databinding.ActivityHomeBinding
import com.example.notes.models.AllObjects.UID
import com.example.notes.models.AllObjects.database
import com.example.notes.models.NotesData
import com.example.notes.recycler.NotesAdapter
import com.example.notes.recycler.SwipeToDeleteCallback
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
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
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        binding.toolbar.inflateMenu(R.menu.menu_home)
    }
    private fun clickListeners() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.deleteAll) {
            if(list.isEmpty()){
                Snackbar.make(findViewById(android.R.id.content), "No Notes Found!", Snackbar.LENGTH_LONG).show()
              return true
            }
            showDialogBox()
            return true
        }
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut()
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}



