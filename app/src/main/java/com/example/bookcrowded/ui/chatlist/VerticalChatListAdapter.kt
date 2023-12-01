package com.example.bookcrowded.ui.chatlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bookcrowded.R
import com.example.bookcrowded.databinding.ItemChatListBinding
import com.example.bookcrowded.ui.dto.ChatListItem
import com.google.firebase.storage.FirebaseStorage

/**
 * AdItem RecyclerView Adapter
 */

class VerticalChatListAdapter (
    private val itemClickListener: VerticalChatListAdapter.OnItemClickListener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    //메인 데이터 셋
    private lateinit var items: List<ChatListViewModel.Companion.ReceivedChatItemViewData>

    fun build(i: List<ChatListViewModel.Companion.ReceivedChatItemViewData>): VerticalChatListAdapter {
        items = i
        return this
    }

    class VerticalChatListViewHolder(val binding: ItemChatListBinding, private val listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatListViewModel.Companion.ReceivedChatItemViewData) {
            binding.root.tag = "ChatItem"
            binding.nameText.text = item.bookTitle
            binding.descriptionText.text = item.description
            binding.senderText.text = item.senderId
//            binding.nameText.text = item.id
//            binding.categoryText.text = item.category

            binding.root.setOnClickListener {
                listener?.onClick(binding.root, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            VerticalChatListViewHolder = VerticalChatListViewHolder(
        ItemChatListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), this.itemClickListener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VerticalChatListViewHolder).bind(items[position])

        val item = items[position]
        val imageUrl = if (item.image.isNullOrEmpty()) {
            // 디폴트 이미지 URL 또는 리소스 ID
            R.drawable.no_photo8
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
                .into(holder.binding.bookImage)
        } else {
            // Firebase 스토리지의 이미지 로딩
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl.toString())
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(holder.itemView.context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(holder.binding.bookImage)
            }.addOnFailureListener {
                // 이미지 로드 실패 시 디폴트 이미지로 대체
                Glide.with(holder.itemView.context)
                    .load(R.drawable.no_photo8)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(holder.binding.bookImage)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}

