package com.example.willgo.data

import kotlinx.serialization.Serializable

@Serializable
data class WillGoRequest(
    val userRequesting: String,
    val userRequested: String,
    val state: String,
)
