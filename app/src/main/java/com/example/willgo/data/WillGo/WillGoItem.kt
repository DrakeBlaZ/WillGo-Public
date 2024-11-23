package com.example.willgo.data.WillGo

import kotlinx.serialization.Serializable

@Serializable
data class WillGoItem(
    val nickname: String,
    var isSelected: Boolean?,
    val name: String?,
    val followers: Int?,

    )
