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

    var loggedInUserName: String = ""

    fun getUserInfoById(userEmail: String) {
        viewModelScope.launch {
            when (val result = userRepository.getDocumentsByField("email", userEmail)) {
                is RepoResult.Success -> {
                    _userResult.postValue(result.data.firstOrNull()) // 사용자 정보를 받아오면 _userResult 업데이트
                    loggedInUserName = result.data.firstOrNull()?.name.orEmpty() // 현재 로그인한 사용자의 이름 업데이트
                }

                is RepoResult.Error -> {
                    // 에러 처리
                }
            }
        }
    }
}