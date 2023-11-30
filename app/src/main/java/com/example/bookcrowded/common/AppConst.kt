package com.example.bookcrowded.common

interface AppConst {

    interface KEY {
        companion object {
            const val SHOW_ID = "Show_id"
            const val ACCESS_TOKEN = "AccessToken"
            const val REFRESH_TOKEN = "RefreshToken"
            const val ITEM_ID = "Item_id"
            const val USER_ID = "User_id"
            const val CHAT_ID = "Chat_id"
            const val SENDER_ID = "Sender_id"
            const val TITLE = "title"
            const val PRICE = "price"
        }
    }

    interface FIREBASE {
        companion object {
            const val CHAT_LIST = "CHAT_LIST"
            const val SELL_ITEM = "SellItem"
            const val IMAGE_URL = "gs://bookbookmarket-f6266.appspot.com"
        }
    }
}