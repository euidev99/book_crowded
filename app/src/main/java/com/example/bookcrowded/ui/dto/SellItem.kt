package com.example.bookcrowded.ui.dto

data class SellItem(
    val id: String = "", //아이템 아이디
    var title: String = "",
    var price: String = "",
    val sellerEmail: String = "",
    var description: String = "",
    val image: String = "", //URL or BitmapImage
    var favorite: Boolean = false,
    val upLoadDate: String = "", // 뭐 나중에 쓰면 쓰고..
    var sold: Boolean = false
)
