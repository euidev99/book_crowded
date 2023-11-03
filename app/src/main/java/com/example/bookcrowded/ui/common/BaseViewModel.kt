package com.example.bookcrowded.ui.common

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


/**
 * 데이터 가져올 때 사용할 인터페이스 함수로 선언
 */
interface ViewModelBase {
    fun refreshData()
}

abstract class BaseViewModel() : ViewModel() {

    /**
     * 추상 메서드로 데이터 가져올 때, 사용
     */
    abstract fun refreshData()

    //공통화는 나중에 고민좀...
    // LiveData를 사용하여 Result<Data>를 감싼다.
//    private val _dataResult = MutableLiveData<Result<Any>>()
//
//    // 뷰에서 접근할 때 사용되는 LiveData
//    val dataResult: LiveData<Result<Any>> get() = _dataResult
//
//    init {
//        refreshData()
//    }


//    fun refreshData() {
//        viewModelScope.launch {
//            try {
////                _dataResult.value = repository.refreshData()
//            } catch (e: Exception) {
//                Log.e("ViewModel","Failed to load Data!")
//            }
//        }
//    }

//    팩토리가 과연 액티비티마다 context 를 그때그때 가져가는게 맞는가?
//    class Factory(val context: Context) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(BaseViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return BaseViewModel(context) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewmodel")
//        }
//    }

}