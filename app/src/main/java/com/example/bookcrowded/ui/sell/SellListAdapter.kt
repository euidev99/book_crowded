package com.example.bookcrowded.ui.sell

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bookcrowded.R
import com.example.bookcrowded.databinding.ItemVerticalBinding
import com.example.bookcrowded.ui.detail.DetailActivity
import com.example.bookcrowded.ui.dto.SellItem
import com.google.firebase.storage.FirebaseStorage



class SellListAdapter(private val items: List<SellItem>) : RecyclerView.Adapter<SellListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemVerticalBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sellItem: SellItem) {
            binding.titleText.text = sellItem.title
            binding.priceText.text = "₩ ${sellItem.price}"


            val imageUrl = if (sellItem.image.isNullOrEmpty()) {
                R.drawable.no_photo8 // 디폴트 이미지 리소스 ID
            } else {
                "gs://bookbookmarket-f6266.appspot.com/image/${sellItem.image}"
            }

            if (sellItem.image.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(binding.mainImage)
            } else {
                val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl.toString())
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(binding.root.context)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .into(binding.mainImage)
                }.addOnFailureListener {
                    Glide.with(binding.root.context)
                        .load(R.drawable.no_photo8)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .into(binding.mainImage)
                }
            }

            // 아이템 클릭 이벤트 등록 (필요한 경우)
            binding.root.setOnClickListener {
                // 클릭 시 DetailActivity로 이동
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.ITEM_ID, sellItem.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}
