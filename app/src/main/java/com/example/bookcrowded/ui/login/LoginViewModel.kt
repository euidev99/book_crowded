package com.example.bookcrowded.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.common.listener.ProgressUIInterface
import com.example.bookcrowded.ui.dto.UserInfo
import com.example.bookcrowded.ui.home.viewdata.HomeItemCategory
import com.example.bookcrowded.ui.home.viewdata.HomeViewData
import com.example.bookcrowded.ui.home.viewdata.SellItemViewData
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {

    private val TAG = "LoginViewModel"

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    fun logIn(email: String, passwd: String) {
        progressListener?.showProgressUI()

        val userRepository = BaseRepository("UserInfo", UserInfo::class.java)
        viewModelScope.launch {
            when (val result = userRepository.getDocumentsByField("email", email)) {
                is RepoResult.Success -> {
                    val dataList = result.data
                    if (dataList.isEmpty()) {
                        _loginResult.postValue(false)
                        progressListener?.hideProgressUI()
                        return@launch
                    }

                    if (dataList[0].password == passwd) {
                        //Login Success
                        _loginResult.postValue(true)
                        progressListener?.hideProgressUI()
                    } else {
                        _loginResult.postValue(false)
                        progressListener?.hideProgressUI()
                    }
                }
                is RepoResult.Error -> {
                    //Error
                    _loginResult.postValue(false)
                    progressListener?.hideProgressUI()
                }
            }
        }
    }
}