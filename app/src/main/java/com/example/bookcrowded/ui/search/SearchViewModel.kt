package com.example.bookcrowded.ui.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.dto.SellItem
import com.example.bookcrowded.ui.dto.UserInfo
import kotlinx.coroutines.launch

class SearchViewModel : BaseViewModel() {
    //DTO 타입 가능
    private val _itemList = MutableLiveData<List<SellItem>>()

    // 뷰에서 접근할 때 사용되는 LiveData
    val itemList: LiveData<List<SellItem>> get() = _itemList

    private val itemRepository: BaseRepository<SellItem> = BaseRepository("SellItem", SellItem::class.java)

    fun getItemIdByPosition(position: Int): String {
        var id = ""

        if (_itemList.isInitialized) {
            id = itemList.value?.get(position)?.id.toString()
        }

        return id
    }

    fun getItemList() {
        progressListener?.showProgressUI()

        viewModelScope.launch {
            when (val result = itemRepository.getAllDocuments()) {
                is RepoResult.Success -> {
                    val dataList = result.data
                    if (dataList.isEmpty()) {
                        //데이터 없을 시 처리
                        progressListener?.hideProgressUI()
                    } else {
                        _itemList.postValue(dataList)
                        progressListener?.hideProgressUI()
                    }
                }

                is RepoResult.Error -> {
                    Log.d("asda", result.exception.toString())
                    progressListener?.hideProgressUI()
                }
            }
        }
    }

    fun getSoldItemList(getSold: Boolean) {
        progressListener?.showProgressUI()
        viewModelScope.launch {
            when (val result = itemRepository.getDocumentsByField("sold", getSold)) {
                is RepoResult.Success -> {
                    val dataList = result.data
                    if (dataList.isEmpty()) {
                        //데이터 없을 시 처리
                        progressListener?.hideProgressUI()
                    } else {
                        _itemList.postValue(dataList)
                        progressListener?.hideProgressUI()
                    }
                }

                is RepoResult.Error -> {
                    progressListener?.hideProgressUI()
                }
            }
        }
    }
}