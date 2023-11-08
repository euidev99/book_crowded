package com.example.bookcrowded.ui.regist

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

class RegisterViewModel : BaseViewModel() {

    private val TAG = "RegisterViewModel"

    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> get() = _registerResult

    private val _duplicatedResult = MutableLiveData<Boolean>()
    val duplicatedResult: LiveData<Boolean> get() = _duplicatedResult

    fun checkDuplicated(email: String) {
        val userRepository = BaseRepository("UserInfo", UserInfo::class.java)
        viewModelScope.launch {
            when (val result = userRepository.getDocumentsByField("email", email)) {
                is RepoResult.Success -> {
                    val dataList = result.data
                    if (dataList.isEmpty()) { // 없으면? 중복 아님
                        _duplicatedResult.postValue(false)
                        progressListener?.hideProgressUI()
                        return@launch
                    } else { // 있으면? 중복
                        _duplicatedResult.postValue(true)
                        progressListener?.hideProgressUI()
                    }

                }
                is RepoResult.Error -> { //얜 그냥 실패
                    _duplicatedResult.postValue(false)
                    progressListener?.hideProgressUI()
                }
            }
        }
    }

    fun register(id: String, email: String, passwd: String, name: String, birth: Int) {
        progressListener?.showProgressUI()

        val userRepository = BaseRepository("UserInfo", UserInfo::class.java)
        viewModelScope.launch {

            when (val result = userRepository.addDocument(UserInfo(birth, email, id, name, passwd))) {
                is RepoResult.Success -> {
                    _registerResult.postValue(result.data)
                    progressListener?.hideProgressUI()
                }

                is RepoResult.Error -> {
                    _registerResult.postValue(false)
                    progressListener?.hideProgressUI()
                }
            }
        }
    }
}