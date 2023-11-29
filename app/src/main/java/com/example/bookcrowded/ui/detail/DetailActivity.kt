package com.example.bookcrowded.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ListPopupWindow
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bookcrowded.R
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.ActivityItemDetailBinding
import com.example.bookcrowded.ui.chat.ChatActivity
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.dto.SellItem
import com.example.bookcrowded.ui.modi.ModificationActivity
import com.google.firebase.storage.FirebaseStorage
import com.laundrycrew.delivery.order.common.CustomPopupListAdapter

class DetailActivity: BaseActivity() {
    private var _binding: ActivityItemDetailBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel.progressListener = this
        mViewModel.itemResult.observe(this) {
            setData(it)
        }



        //아이템 로드
        intent.getStringExtra(ITEM_ID)?.let { mViewModel.getSellItemById(it) }
        setView();
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setData(data: SellItem) {
        //내가 올린 아이템일 경우 옵션 활성화
        if (AuthManager.userEmail != "" && AuthManager.userEmail == data.sellerEmail) {
            binding.moreButton.visibility = View.VISIBLE
            binding.logoImage.visibility = View.GONE
        } else {
            binding.moreButton.visibility = View.GONE
            binding.logoImage.visibility = View.VISIBLE
        }

        if (data.sold) {
            binding.itemStateText.text = "판매 완료"
        } else {
            binding.itemStateText.text = "판매 중"
        }

        binding.itemNameText.text = data.title
        binding.itemSellerText.text = data.sellerEmail
        binding.itemUpdateDateText.text = data.upLoadDate
        binding.itemDescriptionText.text = data.description
        binding.itemPriceText.text = "₩ " + data.price


        //채팅 화면으로 이동하기 전에, 방 정보 생성
        binding.chatButton.setOnClickListener {

            val modifiedRoomId = AuthManager.userEmail.replace(".", "_") + data.id + "_chat"
            val itemId = data.id
            val sender = AuthManager.userEmail
            val receiver = data.sellerEmail

            mViewModel.addChatListWithInfo(modifiedRoomId, itemId, sender, receiver)
        }

        //채팅방 정보 생성 이후에, 채팅방을 실제로 생성
        mViewModel.adChatRoomResult.observe(this) {
            //개설된 채팅방으로 이동
            if (it) {

                val modifiedRoomId = AuthManager.userEmail.replace(".", "_") + data.id + "_chat"
                ChatActivity.startActivityWithArgument(this, modifiedRoomId, data.title, data.price + "원")
            } else {
                //방 개설 실패
            }
        }

        // 책 이미지 URL 구성
        val imageUrl = if (data.image.isNullOrEmpty()) {
            R.drawable.no_photo6 // 디폴트 이미지 리소스 ID
        } else {
            "gs://bookbookmarket-f6266.appspot.com/image/${data.image}"
        }

        // Firebase 스토리지 참조 또는 디폴트 이미지 로딩
        if (data.image.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(binding.mainImage)
        } else {
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl.toString())
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(this)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(binding.mainImage)
            }.addOnFailureListener {
                Glide.with(this)
                    .load(R.drawable.no_photo6)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .into(binding.mainImage)
            }
        }
    }

    private fun setView() {
        binding.moreButton.setOnClickListener {
            showCustomPopupListView(it)
        }

        //backButton으로 디테일 화면 종료
        binding.backButton.setOnClickListener {
            finish()
        }

        //좋아요 버튼
        val itemId = intent.getStringExtra(ITEM_ID)

        binding.favButton.setOnClickListener {
            val isFavorite = mViewModel.itemResult.value?.favorite ?: false
            if (isFavorite) {
                binding.favButton.setImageResource(R.drawable.favorite)
                // ViewModel을 통해 좋아요 상태를 해제하는 로직
            } else {
                binding.favButton.setImageResource(R.drawable.favorite_selected)
                // ViewModel을 통해 좋아요 상태로 만드는 로직
            }
            // 아이콘 업데이트 후 ViewModel에서 좋아요 상태 토글
            itemId?.let { mViewModel.toggleFavorite(it) }
        }
    }

    private fun showCustomPopupListView(anchorView: View) {
        val popupList: MutableList<String> = ArrayList()
        popupList.add("글 수정하기")
        popupList.add("글 삭제하기")

        val adapter = CustomPopupListAdapter(this, popupList)
        val listPopupWindow = ListPopupWindow(this, null, 0, R.style.CustomListPopupWindowStyle)

        listPopupWindow.setAdapter(adapter)
        listPopupWindow.anchorView = anchorView
        listPopupWindow.setDropDownGravity(Gravity.END)
        listPopupWindow.width = 400
        listPopupWindow.height = ListPopupWindow.WRAP_CONTENT

        listPopupWindow.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> {
                    // "글 수정하기" 클릭 시
                    val itemId = intent.getStringExtra(ITEM_ID)
                    itemId?.let {
                        // ModificationActivity 시작
                        ModificationActivity.startActivityWithItemId(this, it)
                    }
                }
                1 -> {
//                    //삭제하기
//                    ModificationActivity.startActivity(this)
                }
            }

            listPopupWindow.dismiss() //동작 처리
        })

        listPopupWindow.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }

    companion object {
        // 액티비티를 시작하는 함수 정의
        private const val ITEM_ID = "id"
        fun startActivity(context: Context) {
            val intent = Intent(context, DetailActivity::class.java)
            context.startActivity(intent)
        }

        fun startActivityWithItemId(context: Context, itemId: String) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(ITEM_ID, itemId)
            context.startActivity(intent)
        }
    }
}
