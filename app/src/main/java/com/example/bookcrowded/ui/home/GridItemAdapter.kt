package com.example.bookcrowded.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookcrowded.databinding.ItemGridBinding
import com.example.bookcrowded.databinding.ItemHorizontalBinding
import com.example.bookcrowded.ui.home.viewdata.SellItemViewData

/**
 * Home 메인 탭에서의
 * Sell Item 그리드 리사이클러 뷰 어댑터
 */

class GridItemAdapter (
    private val items: ArrayList<SellItemViewData> = arrayListOf(),
    private val itemClickListener: GridItemAdapter.OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    class GridItemViewHolder(val binding: ItemGridBinding, private val listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SellItemViewData) {
            binding.root.tag = "GridItem"
            binding.priceText.text = item.price
            binding.root.setOnClickListener { listener?.onClick(binding.root, adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            GridItemViewHolder = GridItemViewHolder(
                ItemGridBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), this.itemClickListener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GridItemViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}