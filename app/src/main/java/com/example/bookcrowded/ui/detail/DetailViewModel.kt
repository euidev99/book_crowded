package com.example.bookcrowded.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.dto.SellItem
import kotlinx.coroutines.launch

class DetailViewModel : BaseViewModel() {
    private val TAG = "DetailViewModel"
    private var itemId = ""
    private var isFavorite = MutableLiveData<SellItem>()
    private val _itemResult = MutableLiveData<SellItem>()
    val itemResult: LiveData<SellItem> get() = _itemResult
    private var detailRepository: BaseRepository<SellItem> = BaseRepository("SellItem", SellItem::class.java)

    fun getSellItemById(itemId: String) {
        viewModelScope.launch {
            //전체 데이터 가져오기
            when (val result = detailRepository.getDocumentsByField("id", itemId)) {
                is RepoResult.Success -> {
                    val dataList = result.data
                    if (dataList.isNotEmpty()) { //아이템이 있을 경우에 하나만
                        _itemResult.postValue(dataList[0])
                    }
                }
                is RepoResult.Error -> {
                    //error or retry
                }
            }
        }
    }
}