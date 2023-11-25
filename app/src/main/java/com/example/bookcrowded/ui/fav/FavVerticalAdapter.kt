package com.example.bookcrowded.ui.fav

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookcrowded.databinding.ItemVerticalBinding
import com.example.bookcrowded.ui.dto.SellItem

class FavVerticalAdapter(
    private var items: List<SellItem> = arrayListOf(),
    private val itemClickListener: FavVerticalAdapter.OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun submitChatMessages(items: List<SellItem>) {
        this.items = items
        this.notifyDataSetChanged()
    }

    inner class VerticalViewHolder(private val binding: ItemVerticalBinding, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SellItem) {
            binding.titleText.text = item.title
            binding.priceText.text = item.price
            binding.root.setOnClickListener { itemClickListener.onClick(binding.root, adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            VerticalViewHolder  =  VerticalViewHolder(
        ItemVerticalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), this.itemClickListener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FavVerticalAdapter.VerticalViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}