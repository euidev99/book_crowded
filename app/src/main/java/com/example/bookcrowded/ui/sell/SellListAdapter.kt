package com.example.bookcrowded.ui.sell

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookcrowded.databinding.ItemVerticalBinding
import com.example.bookcrowded.ui.dto.SellItem

class SellListAdapter(private val items: List<SellItem>) : RecyclerView.Adapter<SellListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sellItem: SellItem) {
            binding.titleText.text = sellItem.title
            binding.priceText.text = "₩ ${sellItem.price}"
            // 이미지 로딩 등 추가 구현이 필요할 수 있음
            // 예: Glide.with(binding.root.context).load(sellItem.imageUrl).into(binding.imageView)

            // 아이템 클릭 이벤트 등록 (필요한 경우)
            binding.root.setOnClickListener {
                // 클릭 시 동작
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}
