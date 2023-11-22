package com.example.bookcrowded.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookcrowded.databinding.ItemGridSellItemBinding
import com.example.bookcrowded.ui.dto.SellItem

/**
 * Chat RecyclerView Adapter
 */

class SearchGridAdapter (
    private var items: List<SellItem> = arrayListOf(),
    private val itemClickListener: SearchGridAdapter.OnItemClickListener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitChatMessages(items : List<SellItem>) {
        this.items = items
        this.notifyDataSetChanged()
    }

    class GridViewHolder(private val binding: ItemGridSellItemBinding, private val listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SellItem) {
            binding.titleText.text = item.title
            binding.priceText.text = item.price
            binding.root.setOnClickListener { listener?.onClick(binding.root, adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            GridViewHolder = GridViewHolder(
                ItemGridSellItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), this.itemClickListener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GridViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

