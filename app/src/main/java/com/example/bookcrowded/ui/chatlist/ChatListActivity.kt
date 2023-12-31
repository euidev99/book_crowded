package com.example.bookcrowded.ui.chatlist

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookcrowded.common.AppConst
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.ActivityChatListBinding
import com.example.bookcrowded.ui.chat.ChatActivity
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.dto.ChatListItem

class ChatListActivity: BaseActivity() {

    private var _binding: ActivityChatListBinding? = null
    private val binding get() = _binding!!

    private val chatListViewModel: ChatListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //챗 목록 옵저빙
        chatListViewModel.viewDataList.observe(this) {
            //완료 시 리사이클러 뷰 셋
            setAdapter(it)
        }

        chatListViewModel.progressListener = this

        setView()
    }

    private fun setAdapter(data: List<ChatListViewModel.Companion.ReceivedChatItemViewData>) {
        if (data.isEmpty()) {
            binding.noneText.visibility = View.VISIBLE
        } else {
            binding.noneText.visibility = View.GONE
        }

        binding.recyclerView.apply {
            adapter = VerticalChatListAdapter(object: VerticalChatListAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    /**
                     * 버튼 클릭 시 이벤트
                     */
                    Log.d("Seki", "MainClick $position View: ${v.tag}")
                    val chatData = data[position]
                    ChatActivity.startActivityWithArgument(context, chatData.chatRoomId, chatData.bookTitle, chatData.price + "원")
                }
            }).build(data)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setView(){

        binding.noneText.visibility = View.VISIBLE
        //뒤로가기 버튼 클릭 이벤트
        binding.backButton.setOnClickListener {
            // 현재 ChatActivity를 종료하고 이전 화면으로 돌아감
            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }

    companion object {
        // 액티비티를 시작하는 함수 정의
        fun startActivity(context: Context) {
            val intent = Intent(context, ChatListActivity::class.java)
            context.startActivity(intent)
        }

        fun startActivityWithChatId(context: Context, chatId: String) {
            val intent = Intent(context, ChatListActivity::class.java)
            intent.putExtra(AppConst.KEY.SHOW_ID, chatId)
            context.startActivity(intent)
        }
    }
}