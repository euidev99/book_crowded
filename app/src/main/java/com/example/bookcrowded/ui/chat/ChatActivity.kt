package com.example.bookcrowded.ui.chat

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookcrowded.common.AppConst
import com.example.bookcrowded.databinding.ActivityChatBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.dto.ChatMessage

class ChatActivity: BaseActivity() {

    private var _binding: ActivityChatBinding? = null
    private val binding get() = _binding!!

    private val chatViewModel: ChatViewModel by viewModels()

    private var isSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatViewModel.progressListener = this
        intent.getStringExtra(AppConst.KEY.USER_ID)?.let { chatViewModel.setRepo(it) }
        chatViewModel.chatResult.observe(this) { success ->
            if (success) {
                // 전송 성공 시의 처리
                Toast.makeText(this, " 성공!", Toast.LENGTH_SHORT).show()

            } else {
                // 전송 실패 시의 처리
                Toast.makeText(this, "전송 실패", Toast.LENGTH_SHORT).show()
            }
        }

        chatViewModel.chatMessages.observe(this) {
            setAdapter(it)
        }

        setView()
    }

    private fun setAdapter(itemList: List<ChatMessage>) {
        if (isSet) {
            (binding.recycler.adapter as VerticalChatAdapter).submitChatMessages(itemList)
            binding.recycler.smoothScrollToPosition(itemList.size - 1)
        } else {
            isSet = true
            binding.recycler.apply {
                adapter =
                    VerticalChatAdapter(
                        items = itemList,
                        itemClickListener = (object :
                            VerticalChatAdapter.OnItemClickListener {

                            override fun onClick(v: View, position: Int) {
                                //ItemClick
                            }
                        })
                    )
                layoutManager = LinearLayoutManager (context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    //버튼 이벤트나 초기 이벤트 처리
    private fun setView() {
        //로그인 버튼
        binding.sendButton.setOnClickListener {
            val text: String = binding.inputEditText.text.toString()
            if (text.isNotEmpty()) {
                chatViewModel.sendChat(text)
                binding.inputEditText.setText("") //전송 후 공백
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }

    companion object {
        // 액티비티를 시작하는 함수 정의
        fun startActivity(context: Context) {
            val intent = Intent(context, ChatActivity::class.java)
            context.startActivity(intent)
        }

        fun startActivityWithChatId(context: Context, chatId: String) {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(AppConst.KEY.SHOW_ID, chatId)
            context.startActivity(intent)
        }
    }
}