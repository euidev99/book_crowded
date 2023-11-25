package com.example.bookcrowded.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.ActivityItemDetailBinding
import com.example.bookcrowded.ui.chat.ChatActivity
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.dto.SellItem

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
        binding.itemUpdateDateText.text = data.upLoadDate
        binding.itemDescriptionText.text = data.description
        binding.itemPriceText.text = "₩ " + data.price
    }

    private fun setView() {
        //채팅 화면으로 이동
        binding.chatButton.setOnClickListener {
            ChatActivity.startActivity(this)
        }
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