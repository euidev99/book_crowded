package com.example.bookcrowded.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.dto.ItemsByCategory
import com.example.bookcrowded.ui.dto.UserInfo
import com.example.bookcrowded.ui.home.viewdata.HomeItemCategory
import com.example.bookcrowded.ui.home.viewdata.HomeViewData
import com.example.bookcrowded.ui.home.viewdata.SellItem
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {

    private val TAG = "HomeViewModel"

    //DTO 타입 가능
    private val _dataResult = MutableLiveData<RepoResult<Any>>()

    // 뷰에서 접근할 때 사용되는 LiveData
    val publicData: LiveData<RepoResult<Any>> get() = _dataResult

//    private val repository: BaseRepository<String> = BaseRepository()

    //그냥 연동해서 사용할 뷰모델 관련 샘플
    val textSample = MutableLiveData<String>(TAG)

    private val _homeViewData = MutableLiveData<HomeViewData>()
    val publicHomeData: LiveData<HomeViewData> get() = _homeViewData
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

    data class Category (
        val category: String
    )

    fun getAllUsers() {
        val userRepository = BaseRepository("test", ItemsByCategory::class.java)
        viewModelScope.launch {
            val sellItem = com.example.bookcrowded.ui.dto.SellItem("id", 99, "name", false)
            val sampleArrayList: ArrayList<com.example.bookcrowded.ui.dto.SellItem> = ArrayList()
            sampleArrayList.add(sellItem)
            sampleArrayList.add(sellItem)
            sampleArrayList.add(sellItem)
            sampleArrayList.add(sellItem)

//            userRepository.addDocument(
//                ItemsByCategory("aa",
//                    sampleArrayList,
//                    1
//                )
//            )
//            when (val result = userRepository.getAllDocuments()) {
//                is RepoResult.Success -> {
//                    // 성공적으로 데이터를 가져왔을 때
//                    val dataList = result.data
//                    Log.d("asdasd", "dataList : " + dataList.size)
//                    for (item in dataList) {
////                        Log.d("asdasd", "item : " + item.email)
//                    }
//                }
//                is RepoResult.Error -> {
//                    // 데이터 가져오기 실패 시 처리
//                    val exception = result.exception
//                    Log.d("asdasd","Aaaaa" + exception)
//                }
//            }
        }
    }

    //set Sample Data
    fun setSampleData() {
        val sellItem = SellItem("ID","Title","10,000","SellerName","Description","",false)

        val sellItemArrayList = ArrayList<SellItem>(10)

        for (i: Int in 1..10) {
            sellItemArrayList.add(sellItem)
        }

        val pagingCategoryData = HomeItemCategory.PagingCategoryData("PAGING", sellItemArrayList)
        val horizontalCategoryData = HomeItemCategory.HorizontalCategoryData("HORIZONTAL", sellItemArrayList)

        val viewData = HomeViewData(ArrayList())
        viewData.itemData.add(pagingCategoryData)
        viewData.itemData.add(horizontalCategoryData)

        this._homeViewData.postValue(viewData)

    }

}