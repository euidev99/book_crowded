package com.example.bookcrowded.ui.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import kotlinx.coroutines.launch

class SearchViewModel : BaseViewModel() {

    private val TAG = "SearchViewModel"

    //DTO 타입 가능
    private val _dataResult = MutableLiveData<RepoResult<Any>>()

    // 뷰에서 접근할 때 사용되는 LiveData
    val publicData: LiveData<RepoResult<Any>> get() = _dataResult

//    private val repository: BaseRepository<String> = BaseRepository()


    //그냥 연동해서 사용할 뷰모델 관련 샘플
    val textSample = MutableLiveData<String>(TAG)

//    override fun refreshData() {
//        Log.d(TAG, ">> refreshData")
//
//        viewModelScope.launch {
//            try {
////                _dataResult.value = repository.refreshData()
//            } catch (e: Exception) {
//                Log.e("ViewModel","Failed to load Data!")
//            }
//        }
//    }
}