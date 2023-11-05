package com.example.bookcrowded.ui.dto

data class SellItem(
    val id: String,
    val title: String,
    val price: String,
    val sellerName: String,
    val description: String,
    val image: String = "", //URL or BitmapImage
    val isFavorite: Boolean = false
)
