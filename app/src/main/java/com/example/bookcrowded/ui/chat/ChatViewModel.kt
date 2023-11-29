package com.example.bookcrowded.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.common.BaseRealTimeRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.dto.ChatMessage
import com.google.rpc.context.AttributeContext.Auth
import kotlinx.coroutines.launch

class ChatViewModel : BaseViewModel() {

    private val TAG = "ChatViewModel"

    private var chatId = ""

    private val _chatResult = MutableLiveData<Boolean>()
    val chatResult: LiveData<Boolean> get() = _chatResult

    //채팅 리스트
    private val _chatMessages = MutableLiveData<List<ChatMessage>>()
    val chatMessages: LiveData<List<ChatMessage>> get() = _chatMessages

    private lateinit var chatRepository: BaseRealTimeRepository<ChatMessage>

    fun sendChat(text: String) {
        progressListener?.showProgressUI()

        val message = ChatMessage(text, AuthManager.userEmail, "")

        viewModelScope.launch {
            when (val result = chatRepository.add(message)) {
                is RepoResult.Success -> {
                    _chatResult.postValue(result.data)
                    progressListener?.hideProgressUI()
                }

                else -> {
                    _chatResult.postValue(false)
                    progressListener?.hideProgressUI()
                }
            }
        }
    }

    fun setRepo(chatId: String) {
        chatRepository = BaseRealTimeRepository(chatId, ChatMessage::class.java)

        viewModelScope.launch {
            if (chatRepository.getAll().isEmpty()) {
                return@launch
            } else {
                _chatMessages.postValue(chatRepository.getAll())
            }
        }

        //새로운 채팅방이 생겼을 경우, 메세지 추가

        // 새로운 메시지 감지 시 UI 갱신
        chatRepository.addMessageListener { messages ->
            _chatMessages.postValue(messages)
            progressListener?.hideProgressUI()
        }
    }
}