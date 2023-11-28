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

    fun addChatList(text: String, seller: String) {
        progressListener?.showProgressUI()

        //실제로는 내 위치
        val chatList = ChatListItem("testForSubChat", "관심사_1", "구매자", "전공책")

        viewModelScope.launch {

            when (val result = chatRepository.addWithId(chatList, seller, AuthManager.userId )) {
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
            _chatMessages.value = chatRepository.getAllWithSubId(AuthManager.userId)
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

//    fun addUserAdListWithAdUrl(receiveUsedId: String, url: String) {
//        progressListener?.showProgressUI()
//        val adListRepository = BaseRealTimeRepository(receiveUsedId, String::class.java)
//
//        viewModelScope.launch {
//            adListRepository.addWithId(url, receiveUsedId + "_" + AuthManager.userId)
//            progressListener?.hideProgressUI()
//        }
//    }

//    fun getChatList(email: String, passwd: String) {
//        progressListener?.showProgressUI()
//
//        val chatListRepository = BaseRepository("UserInfo", ChatListItem::class.java)
//        viewModelScope.launch {
//            when (val result = chatListRepository.getAllDocuments()) {
//                is RepoResult.Success -> {
//
//                }
//
//                is RepoResult.Error -> {
//
//                }
//            }

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
//        }
//    }


}