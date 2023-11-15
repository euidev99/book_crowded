package com.laundrycrew.delivery.order.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.bookcrowded.R

/**
 * 커스텀 팝업에서 아이템 사용할 목적으로 만들어둔 어댑터
 */
class CustomPopupListAdapter(private val context: Context, private val data: List<String>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): String {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var updatedConvertView = convertView

        if (updatedConvertView == null) {
            updatedConvertView = LayoutInflater.from(context).inflate(R.layout.item_custom_popup, parent, false)
        }

        val textView: TextView = updatedConvertView!!.findViewById(R.id.text)
        val line: View = updatedConvertView.findViewById(R.id.line)

        val item = data[position]
        textView.text = item

        if (position == count - 1) {
            line.visibility = View.GONE
        } else {
            line.visibility = View.VISIBLE
        }

        return updatedConvertView
    }
}
