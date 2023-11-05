package com.example.bookcrowded.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookcrowded.databinding.ItemHorizontalBinding
import com.example.bookcrowded.ui.home.viewdata.SellItemViewData

/**
 * Home 메인 탭에서의
 * Sell Item 가로 리사이클러 뷰 어댑터
 */

class HorizontalItemAdapter (
    private val items: ArrayList<SellItemViewData> = arrayListOf(),
    private val itemClickListener: HorizontalItemAdapter.OnItemClickListener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    class HorizontalItemViewHolder(val binding: ItemHorizontalBinding, private val listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SellItemViewData) {
            binding.root.tag = "HorizontalItem"
            binding.titleText.text = item.title
            binding.root.setOnClickListener { listener?.onClick(binding.root, adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            HorizontalItemViewHolder = HorizontalItemViewHolder(
                ItemHorizontalBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), this.itemClickListener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HorizontalItemViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}