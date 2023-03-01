package com.example.notes.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.example.notes.models.AllObjects.database
import com.example.notes.models.AllObjects.notesData
import com.example.notes.models.NotesData
import com.example.notes.databinding.ActivityAddNoteBinding
import com.example.notes.models.AllObjects.UID
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private var currentTimestamp = System.currentTimeMillis().toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
        clickListeners()

    }
    private fun setDate() {
        binding.tvDate.text = SimpleDateFormat("MMMM dd, yyyy hh:mm:ss").format(Date())
    }
    @SuppressLint("SetTextI18n")
    private fun setData() {
        if(notesData.dateTime.equals("")){
            Log.i("imcalled", "fafafadf")
            setDate()
            return
        }
        binding.tvTitle.text = Editable.Factory.getInstance().newEditable( notesData.title.toString())
        binding.etDesc.text = Editable.Factory.getInstance().newEditable(notesData.desc)
        binding.tvDate.text = Editable.Factory.getInstance().newEditable(notesData.dateTime)
        currentTimestamp = notesData.timeStamp.toString()
        binding.tvHeader.text = "Edit Note"
    }
    private fun clickListeners() {
        binding.bSave.setOnClickListener {
            saveData()

        }
        binding.toolbar.setNavigationOnClickListener {
            notesData = NotesData()
            finish()
        }
    }
    private fun saveData() {
        val des = binding.etDesc.text.toString()
        val title = binding.tvTitle.text.toString()
        if(title == "" || des == ""){
            Snackbar.make(findViewById(android.R.id.content),
                "Fill all details",
                Snackbar.LENGTH_LONG)
                .show()
            return
        }
        val currentDate = binding.tvDate.text
        val note = NotesData(currentTimestamp, des, title, currentDate.toString())
        database.child(UID).child(currentTimestamp).setValue(note)
        Snackbar.make(findViewById(android.R.id.content),
            "Note Saved",
            Snackbar.LENGTH_LONG)
            .show()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        notesData = NotesData()

    }
}