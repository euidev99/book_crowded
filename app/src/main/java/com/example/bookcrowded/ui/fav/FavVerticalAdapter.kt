package com.example.bookcrowded.ui.fav

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bookcrowded.R
import com.example.bookcrowded.databinding.ItemVerticalBinding
import com.example.bookcrowded.ui.dto.SellItem
import com.google.firebase.storage.FirebaseStorage

class FavVerticalAdapter(
    private var items: List<SellItem> = arrayListOf(),
    private val itemClickListener: FavVerticalAdapter.OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitChatMessages(items: List<SellItem>) {
        this.items = items
        this.notifyDataSetChanged()
    }

    inner class VerticalViewHolder(val binding: ItemVerticalBinding, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SellItem) {
            binding.titleText.text = item.title
            binding.priceText.text = item.price
            binding.sellerNameText.text = item.sellerEmail
            binding.root.setOnClickListener { itemClickListener.onClick(binding.root, adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            VerticalViewHolder  =  VerticalViewHolder(
        ItemVerticalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), this.itemClickListener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position] // 현재 위치의 아이템
        (holder as VerticalViewHolder).bind(item)

        val imageUrl = if (item.image.isNullOrEmpty()) {
            R.drawable.no_photo6 // 디폴트 이미지 리소스 ID
        } else {
            "gs://bookbookmarket-f6266.appspot.com/image/${item.image}"
        }

        // Firebase 스토리지 참조 또는 디폴트 이미지 로딩
        if (item.image.isNullOrEmpty()) {
            // 디폴트 이미지 로딩
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(holder.binding.mainImage)
        } else {
            // Firebase 스토리지의 이미지 로딩
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl.toString())
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(holder.itemView.context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(holder.binding.mainImage)
            }.addOnFailureListener {
                // 이미지 로드 실패 시 디폴트 이미지로 대체
                Glide.with(holder.itemView.context)
                    .load(R.drawable.no_photo6)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(holder.binding.mainImage)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}