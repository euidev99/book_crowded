package com.example.bookcrowded.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import kotlinx.coroutines.launch

class MainViewModel(): BaseViewModel() {

    private val TAG = "MainViewModel"

    //DTO 타입 가능
    private val _dataResult = MutableLiveData<RepoResult<Any>>()

    // 뷰에서 접근할 때 사용되는 LiveData
    val publicData: LiveData<RepoResult<Any>> get() = _dataResult

//    private val repository: BaseRepository<String> = BaseRepository()

    override fun refreshData() {
        Log.d(TAG, ">> refreshData")

        viewModelScope.launch {
            try {
//                _dataResult.value = repository.refreshData()
            } catch (e: Exception) {
                Log.e("ViewModel","Failed to load Data!")
            }
        }
    }
}