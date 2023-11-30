package com.example.bookcrowded.ui.modi

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bookcrowded.R
import com.example.bookcrowded.common.AppConst
import com.example.bookcrowded.common.BaseRealTimeRepository
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.dto.ChatRoomDto
import com.example.bookcrowded.ui.dto.SellItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch

class ModificationViewModel : BaseViewModel() {

    private var itemRepository: BaseRepository<SellItem> = BaseRepository("SellItem", SellItem::class.java)

    private val _itemResult = MutableLiveData<SellItem>()
    val itemResult: LiveData<SellItem> get() = _itemResult

    private val _documentIdResult = MutableLiveData<String>()
    val documentIdResult: LiveData<String> get() = _documentIdResult

    private val _uriResult = MutableLiveData<Uri>()
    val uriResult: LiveData<Uri> get() = _uriResult

    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> get() = _updateResult

    private val _enrollImageResult = MutableLiveData<Boolean>()
    val enrollImageResult: LiveData<Boolean> get() = _enrollImageResult

    //storage
    private val storage = Firebase.storage
    private val storageRef = storage.reference

    fun getImageFromStorageByImageUrl(imageUrl: String) {
        // 책 이미지 URL 구성
        val modiImageUrl = "${AppConst.FIREBASE.IMAGE_URL}/image/${imageUrl}"
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(modiImageUrl)
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            _uriResult.postValue(uri)
            Log.d("asd", uri.lastPathSegment.toString())

        }.addOnFailureListener {
            //실패
            Log.d("asd", it.message.toString())
        }
    }

    fun enrollImageByUri(uri: Uri) {

        Log.d("asd", uri.lastPathSegment.toString())
        val imagesRef = storageRef.child("/image/${uri.lastPathSegment}")

        imagesRef.putFile(uri)
            .addOnSuccessListener {
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    _enrollImageResult.postValue(true)
                }
            }
            .addOnFailureListener { exception ->
                _enrollImageResult.postValue(false)
            }
    }

    fun getDocumentIdById(itemId: String) {
        progressListener?.showProgressUI()

        viewModelScope.launch {
            when (val result = itemRepository.getDocumentIdsByField("id", itemId)) {
                is RepoResult.Success -> {
                    if (result.data.isNotEmpty()) {
                        _documentIdResult.postValue(result.data.get(0))
                    }
                    progressListener?.hideProgressUI()
                }

                is RepoResult.Error -> {

                    progressListener?.hideProgressUI()
                }
            }
        }
    }

    fun updateItemByDocumentId(documentId: String, updates : Map<String, Any>) {
        progressListener?.showProgressUI()

        viewModelScope.launch {
            when (val result = itemRepository.updateFields(documentId, updates)) {
                is RepoResult.Success -> {
                    _updateResult.postValue(result.data)
                    progressListener?.hideProgressUI()
                }

                is RepoResult.Error -> {
                    _updateResult.postValue(false)
                    progressListener?.hideProgressUI()
                }
            }
        }
    }

    fun getSellItemById(itemId: String) {
        progressListener?.showProgressUI()
        viewModelScope.launch {
            //전체 데이터 가져오기
            when (val result = itemRepository.getDocumentsByField("id", itemId)) {
                is RepoResult.Success -> {
                    val dataList = result.data
                    if (dataList.isNotEmpty()) { //아이템이 있을 경우에 하나만
                        _itemResult.postValue(dataList[0])
                        _itemResult.value?.favorite = dataList[0].favorite
                    }
                    progressListener?.hideProgressUI()
                }

                is RepoResult.Error -> {
                    //error or retry
                    progressListener?.hideProgressUI()
                }
            }
        }
    }

}