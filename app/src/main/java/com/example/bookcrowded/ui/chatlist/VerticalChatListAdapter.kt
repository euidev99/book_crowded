package com.example.bookcrowded.ui.chatlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookcrowded.databinding.ItemChatListBinding
import com.example.bookcrowded.ui.dto.ChatListItem

/**
 * AdItem RecyclerView Adapter
 */

class VerticalChatListAdapter (
    private val itemClickListener: VerticalChatListAdapter.OnItemClickListener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    //메인 데이터 셋
    private lateinit var items: List<ChatListItem>

    fun build(i: List<ChatListItem>): VerticalChatListAdapter {
        items = i
        return this
    }

    class VerticalChatListViewHolder(private val binding: ItemChatListBinding, private val listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatListItem) {
            binding.root.tag = "ChatItem"
            binding.nameText.text = item.id
            binding.categoryText.text = item.category

            binding.root.setOnClickListener {
                listener?.onClick(binding.root, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            VerticalChatListViewHolder = VerticalChatListViewHolder(
        ItemChatListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), this.itemClickListener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VerticalChatListViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

