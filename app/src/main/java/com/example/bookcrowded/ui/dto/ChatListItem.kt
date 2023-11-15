package com.example.bookcrowded.ui.dto

data class ChatListItem(
    val id: String? = "id 오류",
    val distance: String? = "0M",
    val category: String? = "관심사",
    val long: Double? = 0.0,
    val lat: Double? = 0.0
)
