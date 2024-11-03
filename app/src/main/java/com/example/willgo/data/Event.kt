package com.example.willgo.data

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Long,
    val description: String?,
    val name_event: String,
    val email_contact: String? = "",
    val phone: Long? = 0,
    val category: Category?,
    val location: String?,
    val date: String? = "",
    val price: Float?,
    val image: String?,
    val duration: Float?,
    val asistance: Long?,
    val type: String? = ""
)
