package com.example.bookcrowded.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.BaseViewModel
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.dto.SellItem
import com.example.bookcrowded.ui.dto.UserInfo
import com.example.bookcrowded.ui.home.viewdata.HomeItemCategory
import com.example.bookcrowded.ui.home.viewdata.HomeViewData
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {

    private val TAG = "HomeViewModel"

    //DTO 타입 가능
    private val _dataResult = MutableLiveData<RepoResult<Any>>()

    // 뷰에서 접근할 때 사용되는 LiveData
    val publicData: LiveData<RepoResult<Any>> get() = _dataResult

    //그냥 연동해서 사용할 뷰모델 관련 샘플
    val textSample = MutableLiveData<String>(TAG)

    private val _homeViewData = MutableLiveData<HomeViewData>()
    val publicHomeData: LiveData<HomeViewData> get() = _homeViewData

    fun getAllUsers() {
        val userRepository = BaseRepository("UserInfo", UserInfo::class.java)
        viewModelScope.launch {
//            val sellItem = com.example.bookcrowded.ui.dto.SellItem("id", 99, "name", false, title = "히히 판매")
//            val sampleArrayList: ArrayList<com.example.bookcrowded.ui.dto.SellItem> = ArrayList()
//            sampleArrayList.add(sellItem)
//            sampleArrayList.add(sellItem)
//            sampleArrayList.add(sellItem)
//            sampleArrayList.add(sellItem)

            when (val result = userRepository.getDocumentsByField("birth",999999)) {
                is RepoResult.Success -> {
                    val dataList = result.data
                    Log.d("asdasd", "dataList : " + dataList.size)
                }
                is RepoResult.Error -> {

                }
            }

            when (val result = userRepository.getAllDocuments()) {
                is RepoResult.Success -> {
                    val dataList = result.data
                    Log.d("asdasd", "dataList : " + dataList.size)
                    for (item in dataList) {
                        Log.d("asdasd", "email : " + item.email)
                    }
                }
                is RepoResult.Error -> {

                }
            }

            when (val result = userRepository.getDocumentById("userInfo")) {
                is RepoResult.Success -> {
                    val dataList = result.data
                    Log.d("asdasd", "Field " + dataList.name)
                }
                is RepoResult.Error -> {

                }
            }

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
        val sellItemArrayList = ArrayList<SellItem>(10)
        val soldSellItem = SellItem("aa","not sold","1000",
            "email","description","adad",false,"2023",false)

        val aaaaaellItem = SellItem("aa","not sold","1000",
            "email","description","adad",false,"2023",false)
        for (i: Int in 1..10) {
            sellItemArrayList.add(soldSellItem)
        }

        val pagingCategoryData = HomeItemCategory.PagingCategoryData("NEW", sellItemArrayList)
        val gridCategoryData = HomeItemCategory.GridCategoryData("On Sale", sellItemArrayList)

        val viewData = HomeViewData(ArrayList())
        viewData.itemData.add(pagingCategoryData)
        viewData.itemData.add(gridCategoryData)

        this._homeViewData.postValue(viewData)
    }
}