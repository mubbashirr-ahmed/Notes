package com.example.notes.models
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object AllObjects{
    var notesData = NotesData()
    var UID:String = ""
    val database = Firebase.database.reference.child("AllNotes")

}