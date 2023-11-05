package com.example.bookcrowded.ui.dto

data class ItemsByCategory(
    val category: String = "중고책",
    val items: ArrayList<SellItem> = ArrayList(),
    val type: Int = 0
)
