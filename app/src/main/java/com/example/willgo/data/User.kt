package com.example.willgo.data

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val nickname: String,
    val name: String,
    val password: String,
    val email: String,
    val followers: Int,
    val followed: Int
) {

}