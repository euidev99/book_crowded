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
import com.example.bookcrowded.ui.dto.SellItem
import com.example.bookcrowded.ui.dto.UserInfo
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.launch

class MyViewModel(): BaseViewModel() {
    private val _userResult = MutableLiveData<UserInfo>()
    val userResult: LiveData<UserInfo> get() = _userResult
    private var userRepository: BaseRepository<UserInfo> = BaseRepository("UserInfo", UserInfo::class.java)


    fun getUserInfoById(userEmail: String) {
        viewModelScope.launch {
            //전체 데이터 가져오기
            when (val result = userRepository.getDocumentsByField("email", userEmail)) {
                is RepoResult.Success -> {

                }

                is RepoResult.Error -> {
                    //error or retry
                }
            }
        }
    }

    init {
        //refreshData()
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