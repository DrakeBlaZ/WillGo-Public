package com.example.willgo.data

data class Event(
    val id: Int,
    val description: String,
    val name_event: String,
    val email_contact: String = "",
    val phone : Int? = 0,
    val category : String,
    val location: String,
    val date: String,
)
