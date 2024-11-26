package com.example.willgo.data

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val userRequesting: String,
    val userRequested: String,
    val state: String,
)
