package com.example.bookcrowded.ui.chatlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.common.BaseRealTimeRepository
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.dto.ChatListItem
import com.example.bookcrowded.ui.dto.ChatMessage
import kotlinx.coroutines.launch

class ChatListViewModel : BaseViewModel() {

    private val TAG = "ChatListViewModel"

    private val _chatUploadResult = MutableLiveData<Boolean>()
    val chatUploadResult: LiveData<Boolean> get() = _chatUploadResult

    //채팅 리스트
    private val _chatMessages = MutableLiveData<List<ChatListItem>>()
    val chatMessages: LiveData<List<ChatListItem>> get() = _chatMessages

    private val chatRepository = BaseRealTimeRepository("Chat", ChatListItem::class.java)

    private val chatListIdRepository = BaseRealTimeRepository("Chat_Id", String::class.java)
    //채팅 id 목록
    private val _chatIdList = MutableLiveData<List<String>>()
    val chatIdList: LiveData<List<String>> get() = _chatIdList

    fun addChatList(itemId: String, seller: String) {
        progressListener?.showProgressUI()

        viewModelScope.launch {
            when (val result = chatListIdRepository.addWithId(itemId, seller + "_" + itemId)) {
                is RepoResult.Success -> {
                    _chatUploadResult.postValue(result.data)
                    progressListener?.hideProgressUI()
                }

                else -> {
                    _chatUploadResult.postValue(false)
                    progressListener?.hideProgressUI()
                }
            }
        }
    }

    init {
        // 초기에 데이터를 가져와서 UI 초기화
        viewModelScope.launch {
//            _chatMessages.value = chatRepository.getAll()
//            _chatMessages.value = chatRepository.getAllWithSubId(AuthManager.userId)
            _chatIdList.value = chatListIdRepository.getAllStartWithId("atest")
        }

        // 새로운 메시지 감지 시 UI 갱신
//        chatListIdRepository.addMessageListener { messages ->
//            _chatIdList.value = messages
//
//            progressListener?.hideProgressUI()
//        }
    }

    fun getChatIdByPosition(position: Int): String {
        return _chatMessages.value?.get(position)?.id.toString()
    }

}