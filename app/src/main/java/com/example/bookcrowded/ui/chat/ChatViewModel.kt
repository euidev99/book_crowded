package com.example.bookcrowded.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.common.BaseRealTimeRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.dto.ChatMessage
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

        val message = ChatMessage(text, "myName","")

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
            _chatMessages.value = chatRepository.getAll()
        }

        //새로운 채팅방이 생겼을 경우, 메세지 추가


        // 새로운 메시지 감지 시 UI 갱신
        chatRepository.addMessageListener { messages ->
            _chatMessages.value = messages
            progressListener?.hideProgressUI()
        }
    }

    init {
        // 초기에 데이터를 가져와서 UI 초기화
//        viewModelScope.launch {
//            _chatMessages.value = chatRepository.getAll()
//        }
//
//        // 새로운 메시지 감지 시 UI 갱신
//        chatRepository.addMessageListener { messages ->
//            _chatMessages.value = messages
//            progressListener?.hideProgressUI()
//        }
    }
}