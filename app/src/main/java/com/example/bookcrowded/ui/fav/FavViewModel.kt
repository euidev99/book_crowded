package com.example.bookcrowded.ui.fav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.dto.SellItem
import kotlinx.coroutines.launch

class FavViewModel : BaseViewModel() {
    private val TAG = "FavViewModel"
    private val _favoriteItemList = MutableLiveData<List<SellItem>>()
    val favoriteItemList: LiveData<List<SellItem>> get() = _favoriteItemList
    private val itemRepository: BaseRepository<SellItem> = BaseRepository("SellItem", SellItem::class.java)

    fun getFavoriteItemList() {
        progressListener?.showProgressUI()

        viewModelScope.launch {
            when (val result = itemRepository.getAllDocuments()) {
                is RepoResult.Success -> {
                    val dataList = result.data.filter { it.isFavorite }
                    if (dataList.isEmpty()) {
                        progressListener?.hideProgressUI()
                    } else {
                        _favoriteItemList.postValue(dataList)
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