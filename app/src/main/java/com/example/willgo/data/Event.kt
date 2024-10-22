package com.example.willgo.data

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Event(
    val id: Int,
    val description: String?,
    val name_event: String,
    val email_contact: String? = "",
    val phone : Int? = 0,
    val category : Category?,
    val location: String?,
    val date: String?,
    val price : Float?
)
