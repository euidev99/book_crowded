package com.example.bookcrowded.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookcrowded.databinding.ItemPagingBinding
import com.example.bookcrowded.ui.home.viewdata.SellItemViewData

/**
 * 생성자로 리스트 받고 리스터 주입,
 * 리스너 미 주입시에 대한 처리는 하지 않음 필수 동작
 */
class PagingItemAdapter(
    private var items: ArrayList<SellItemViewData> = arrayListOf(),
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            PagingItemViewHolder = PagingItemViewHolder(ItemPagingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), this.itemClickListener
    )

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PagingItemViewHolder).bind(items[position])
    }

    class PagingItemViewHolder(
        val binding: ItemPagingBinding,
        private val listener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SellItemViewData) {
            binding.root.tag = "PagingItem"
            binding.mainText.text = item.title
            binding.root.setOnClickListener { listener?.onClick(binding.root, adapterPosition) }
        }
    }
}