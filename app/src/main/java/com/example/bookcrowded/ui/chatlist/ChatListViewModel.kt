package com.example.bookcrowded.ui.chatlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.common.AppConst
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.common.BaseRealTimeRepository
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.dto.ChatListItem
import com.example.bookcrowded.ui.dto.ChatMessage
import com.example.bookcrowded.ui.dto.ChatRoomDto
import com.example.bookcrowded.ui.dto.SellItem
import kotlinx.coroutines.launch

class ChatListViewModel : BaseViewModel() {

    private val TAG = "ChatListViewModel"

    private val _chatUploadResult = MutableLiveData<Boolean>()
    val chatUploadResult: LiveData<Boolean> get() = _chatUploadResult

//    //채팅 리스트
//    private val _chatMessages = MutableLiveData<List<ChatListItem>>()
//    val chatMessages: LiveData<List<ChatListItem>> get() = _chatMessages

    private val chatListIdRepository = BaseRealTimeRepository(AppConst.FIREBASE.CHAT_LIST, ChatRoomDto::class.java)
    //채팅 id 목록
    private val _chatIdList = MutableLiveData<List<ChatRoomDto>>()
    val chatIdList: LiveData<List<ChatRoomDto>> get() = _chatIdList

    private val itemRepository = BaseRepository(AppConst.FIREBASE.SELL_ITEM, SellItem::class.java)

    private val _viewDataList = MutableLiveData<List<ReceivedChatItemViewData>>()
    val viewDataList: LiveData<List<ReceivedChatItemViewData>> get() = _viewDataList

    init {
        // 초기에 데이터를 가져와서 UI 초기화
        progressListener?.showProgressUI()
        viewModelScope.launch {
            val receivedChatList = arrayListOf<ReceivedChatItemViewData>()

            val totalChatRoomIdList = chatListIdRepository.getAll()
            val myChatRoomIdList = arrayListOf<ChatRoomDto>()
            for (item in totalChatRoomIdList) {
                if (item.receiver == AuthManager.userEmail) { //내가 받은 채팅이름일 경우
                    myChatRoomIdList.add(item)
                }
            }

            for (myChatItem in myChatRoomIdList) {
                when (val result = itemRepository.getDocumentsByField("id", myChatItem.itemId)) {
                    is RepoResult.Success -> {
                        if (result.data.isNotEmpty()) {
                            val sellItemData = result.data[0]
                            val viewData = ReceivedChatItemViewData(myChatItem.sender,
                                sellItemData.title,
                                "채팅방이 개설되었습니다.",
                                sellItemData.image,
                                myChatItem.roomId,
                                sellItemData.price)

                            receivedChatList.add(viewData)
                        }
                        progressListener?.hideProgressUI()
                    }
                    is RepoResult.Error -> {
                        //x
                        progressListener?.hideProgressUI()
                    }
                }
            }
            _viewDataList.postValue(receivedChatList)
        }

        // 새로운 메시지 감지 시 UI 갱신
//        chatListIdRepository.addMessageListener { messages ->
//            _chatIdList.value = messages
//
//            progressListener?.hideProgressUI()
//        }
    }

    companion object {
        data class ReceivedChatItemViewData(
            val senderId: String = "",
            val bookTitle: String = "",
            val description: String = "",
            val image: String = "",
            val chatRoomId: String = "",
            val price: String = ""
        )
    }

}