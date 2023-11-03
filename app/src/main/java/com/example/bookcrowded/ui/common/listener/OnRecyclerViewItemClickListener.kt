package com.example.bookcrowded.ui.common.listener

import android.view.View

interface OnRecyclerViewItemClickListener {
    fun onClick(v: View, position: Int)

    //필요에 따라 커스텀
    fun onSubItemClick(v: View, position: Int)
}