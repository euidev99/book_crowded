package com.example.bookcrowded.ui.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookcrowded.databinding.ItemChatLeftBinding
import com.example.bookcrowded.ui.dto.ChatMessage

/**
 * Chat RecyclerView Adapter
 */

class VerticalChatAdapter (
    private var items: List<ChatMessage> = arrayListOf(),
    private val itemClickListener: VerticalChatAdapter.OnItemClickListener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_LEFT = 1
        private const val VIEW_TYPE_RIGHT = 2
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitChatMessages(items : List<ChatMessage>) {
        this.items = items
        this.notifyDataSetChanged()
    }

    class VerticalChatViewHolder(private val binding: ItemChatLeftBinding, private val listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatMessage) {
            binding.root.tag = "ChatItem"
            binding.mainText.text = item.message
            binding.root.setOnClickListener { listener?.onClick(binding.root, adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            VerticalChatViewHolder = VerticalChatViewHolder(
        ItemChatLeftBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), this.itemClickListener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VerticalChatViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

