package com.example.bookcrowded.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ListPopupWindow
import androidx.activity.viewModels
import com.example.bookcrowded.R
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.ActivityItemDetailBinding
import com.example.bookcrowded.ui.chat.ChatActivity
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.dto.SellItem
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


    private fun setData(data: SellItem) {
        if (AuthManager.userId != "" && AuthManager.userEmail == data.sellerEmail) {
            //내가 올린 아이템일 경우
            //binding.editButton.visibility = View.VISIBLE
        } else {
            //binding.editButton.visibility = View.GONE
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
    }

    private fun setView() {
        binding.moreButton.setOnClickListener {
            showCustomPopupListView(it)
        }

        //채팅 화면으로 이동
        binding.chatButton.setOnClickListener {
            ChatActivity.startActivity(this)
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
                // ViewModel을 통해 좋아요 상태를 해제
            } else {
                binding.favButton.setImageResource(R.drawable.favorite_selected)
                // ViewModel을 통해 좋아요 상태로 만듦
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
                    //수정하기
                    //ModificationActivity.startActivity(this)
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
        private const val ITEM_ID = "Item_id"
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
