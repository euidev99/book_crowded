package com.example.bookcrowded.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bookcrowded.databinding.ItemPagingBinding
import com.example.bookcrowded.ui.dto.SellItem
import com.example.bookcrowded.ui.search.SearchGridAdapter
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.withContext

/**
 * 생성자로 리스트 받고 리스터 주입,
 * 리스너 미 주입시에 대한 처리는 하지 않음 필수 동작
 */
class PagingItemAdapter(
    private var items: List<SellItem> = arrayListOf(),
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
        val item = items[position] // 현재 위치의 아이템
        (holder as PagingItemViewHolder).bind(items[position])


    }

    class PagingItemViewHolder(
        val binding: ItemPagingBinding,
        private val listener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SellItem) {
            binding.root.tag = "PagingItem"
            binding.mainText.text = item.title
            binding.root.setOnClickListener { listener?.onClick(binding.root, adapterPosition) }

            // Firebase 스토리지의 이미지 경로 빌드
            val imageUrl = "gs://bookbookmarket-f6266.appspot.com/image/${item.image}" // 예시 경로, 'item.adImageUrl'을 아이템의 실제 이미지 파일 이름으로 교체

            // Firebase 스토리지 참조 가져오기
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)

            // 이미지 URL 가져오기 및 Glide 사용하여 로드
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(binding.root.context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(binding.mainImage)
            }.addOnFailureListener {
                // 이미지 로드 실패 처리
                Log.e("FirebaseImageLoad", "Error loading image", it)
            }
        }
    }
}