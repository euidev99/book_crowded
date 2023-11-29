package com.example.bookcrowded.ui.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.ItemChatLeftBinding
import com.example.bookcrowded.databinding.ItemChatRightBinding
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

    class VerticalChatLeftViewHolder(private val binding: ItemChatLeftBinding, private val listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatMessage) {
            binding.root.tag = "ChatItem"
            binding.mainText.text = item.message
            binding.root.setOnClickListener { listener?.onClick(binding.root, adapterPosition) }
        }
    }

    class VerticalChatRightViewHolder(private val binding: ItemChatRightBinding, private val listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatMessage) {
            binding.root.tag = "ChatItem"
            binding.mainText.text = item.message
            binding.root.setOnClickListener { listener?.onClick(binding.root, adapterPosition) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (items.get(position).name == AuthManager.userEmail) {
            //내가 보낸 메세지
            return VIEW_TYPE_RIGHT
        } else {
            return VIEW_TYPE_LEFT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == VIEW_TYPE_LEFT) {
            return VerticalChatLeftViewHolder(
                ItemChatLeftBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), itemClickListener
            )
        } else {
            return VerticalChatRightViewHolder(
                ItemChatRightBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), itemClickListener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_LEFT) {
            (holder as VerticalChatLeftViewHolder).bind(items[position])
        } else {
            (holder as VerticalChatRightViewHolder).bind(items[position])
        }
    }

    override fun getItemCount(): Int = items.size
}

