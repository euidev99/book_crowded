package com.example.bookcrowded.ui.dto

data class ItemsByCategory(
    val category: String = "",
    val items: ArrayList<SellItem> = ArrayList(),
    val type: Int = 0
)
