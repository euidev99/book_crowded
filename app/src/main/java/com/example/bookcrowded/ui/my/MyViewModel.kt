package com.example.bookcrowded.ui.my

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import kotlinx.coroutines.launch

class MyViewModel(): BaseViewModel() {

    private val TAG = "MyViewModel"

    //DTO 타입 가능
    private val _dataResult = MutableLiveData<RepoResult<Any>>()

    // 뷰에서 접근할 때 사용되는 LiveData
    val publicData: LiveData<RepoResult<Any>> get() = _dataResult

//    private val repository: BaseRepository<String> = BaseRepository(context)


    //그냥 연동해서 사용할 뷰모델 관련 샘플
    val textSample = MutableLiveData<String>(TAG)

    init {
//        refreshData()
    }

    //Context 를 뷰모델이 들고 있는건 안좋으니 보류
//    class Factory(val context: Context) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return MyViewModel(context) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewmodel")
//        }
//    }

//    override fun refreshData() {
//        Log.d(TAG, ">> refreshData")
//
//        viewModelScope.launch {
//            try {
//                textSample.postValue(TAG)
////                _dataResult.value = repository.refreshData()
//            } catch (e: Exception) {
//                Log.e("ViewModel","Failed to load Data!")
//            }
//        }
//    }
}