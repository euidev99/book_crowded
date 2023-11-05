package com.example.bookcrowded.ui.dto

data class SellItem(
    val id: String = "",
    val price: Int = 0,
    val sellerName: String = "",
    val isSoldOut: Boolean = false
)
