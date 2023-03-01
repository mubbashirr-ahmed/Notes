package com.example.notes.models

data class NotesData(
    val timeStamp:String?,
    val desc: String?,
    val title:String?,
    val dateTime:String?
){
    constructor() : this("", "", "", "")
}
