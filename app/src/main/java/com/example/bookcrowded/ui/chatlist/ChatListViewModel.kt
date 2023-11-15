package com.example.bookcrowded.ui.chatlist

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

    private val chatRepository = BaseRealTimeRepository("ChatList", ChatListItem::class.java)

    fun addChatList(text: String) {
        progressListener?.showProgressUI()

        val message = ChatMessage(text, "myName","")
        //실제로는 내 위치
        val chatList = ChatListItem("testForChat", "1M", "Cafe", 0.0, 0.0)

        viewModelScope.launch {
            when (val result = chatRepository.add(chatList)) {
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
            _chatMessages.value = chatRepository.getAll()
        }

        // 새로운 메시지 감지 시 UI 갱신
        chatRepository.addMessageListener { messages ->
            _chatMessages.value = messages
            progressListener?.hideProgressUI()
        }
    }

    fun getChatIdByPosition(position: Int): String {
        return _chatMessages.value?.get(position)?.id.toString()
    }

    fun getChatList(email: String, passwd: String) {
        progressListener?.showProgressUI()

        val chatListRepository = BaseRepository("UserInfo", ChatListItem::class.java)
        viewModelScope.launch {
            when (val result = chatListRepository.getAllDocuments()) {
                is RepoResult.Success -> {

                }

                is RepoResult.Error -> {

                }
            }

//            when (val result = userRepository.getDocumentsByField("email", email)) {
//                is RepoResult.Success -> {
//                    val dataList = result.data
//                    if (dataList.isEmpty()) {
//                        _loginResult.postValue(false)
//                        progressListener?.hideProgressUI()
//                        return@launch
//                    }
//
//                    if (dataList[0].password == passwd) {
//                        //Login Success
//                        _loginResult.postValue(true)
//                        progressListener?.hideProgressUI()
//                    } else {
//                        _loginResult.postValue(false)
//                        progressListener?.hideProgressUI()
//                    }
//                }
//                is RepoResult.Error -> {
//                    //Error
//                    _loginResult.postValue(false)
//                    progressListener?.hideProgressUI()
//                }
//            }
        }
    }


}