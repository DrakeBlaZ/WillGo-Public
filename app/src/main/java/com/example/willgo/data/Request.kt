package com.example.willgo.data

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val userRequesting: String,
    val userRequested: Long,
    val state: String,
    val nickRequested: String,
    val idEvent: Long
)
