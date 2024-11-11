package com.example.willgo.data

import kotlinx.serialization.Serializable

@Serializable
data class WillGo (
    val id_event: Int,
    val users: String
)