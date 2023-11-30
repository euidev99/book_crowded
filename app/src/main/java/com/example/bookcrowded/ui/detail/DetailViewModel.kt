package com.example.bookcrowded.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.common.AppConst
import com.example.bookcrowded.common.BaseRealTimeRepository
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.dto.ChatRoomDto
import com.example.bookcrowded.ui.dto.SellItem
import kotlinx.coroutines.launch

class DetailViewModel : BaseViewModel() {
    private val _itemResult = MutableLiveData<SellItem>()
    val itemResult: LiveData<SellItem> get() = _itemResult
    private var detailRepository: BaseRepository<SellItem> = BaseRepository("SellItem", SellItem::class.java)


    private val chatRoomRepository = BaseRealTimeRepository(AppConst.FIREBASE.CHAT_LIST, ChatRoomDto::class.java)
    //채팅 id 목록
    private val _adChatRoomResult = MutableLiveData<Boolean>()
    val adChatRoomResult: LiveData<Boolean> get() = _adChatRoomResult

    fun getSellItemById(itemId: String) {
        viewModelScope.launch {
            //전체 데이터 가져오기
            when (val result = detailRepository.getDocumentsByField("id", itemId)) {
                is RepoResult.Success -> {
                    val dataList = result.data
                    if (dataList.isNotEmpty()) { //아이템이 있을 경우에 하나만
                        _itemResult.postValue(dataList[0])
                        _itemResult.value?.favorite = dataList[0].favorite
                    }
                }

                is RepoResult.Error -> {
                    //error or retry
                }
            }
        }
    }

    fun addChatListWithInfo(roomId: String , itemId: String , sender: String , receiver: String) {
        progressListener?.showProgressUI()
        val chatRoomDto = ChatRoomDto(roomId, itemId, sender, receiver)

        viewModelScope.launch {
            var duplicatedFlag = false
            val result = chatRoomRepository.getAll()
            for (i in result) {
                if (i.roomId == roomId) {
                    duplicatedFlag = true
                    break
                }
            }
            if (duplicatedFlag) {
                progressListener?.hideProgressUI()
                _adChatRoomResult.postValue(true)
            } else {
                when (val result = chatRoomRepository.add(chatRoomDto)) {
                    is RepoResult.Success -> {
                        _adChatRoomResult.postValue(result.data)
                        progressListener?.hideProgressUI()
                    }

                    else -> {
                        _adChatRoomResult.postValue(false)
                        progressListener?.hideProgressUI()
                    }
                }
            }
        }
    }

    fun updateFavorite(itemId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            val result = detailRepository.updateField(itemId, "favorite", isFavorite)
            if (result is RepoResult.Success) {
                // 업데이트 성공
            } else if (result is RepoResult.Error) {
                // 업데이트 실패
            }
        }
    }

    fun toggleFavorite(itemId: String) {
        val newFavorite = !_itemResult.value?.favorite!!
        _itemResult.value?.favorite = newFavorite
        updateFavorite(itemId, newFavorite)
    }
}