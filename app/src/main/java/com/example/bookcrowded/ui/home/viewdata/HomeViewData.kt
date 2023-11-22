package com.example.bookcrowded.ui.home.viewdata

import com.example.bookcrowded.ui.dto.SellItem
import com.example.bookcrowded.ui.home.HomeMainAdapter


/**
 * Home 화면에서의 메인 리사이클러뷰에 사용할 데이터 양식
 * 각 뷰 타입 지정자 선언
 * 각 세부 데이터는 각각의 아이템 어레이와 뷰 타입을 포함
 * 뷰 타입은 HomeMainAdapter 의 companion 으로 정의
 */
data class HomeViewData(
    val itemData: ArrayList<HomeItemCategory>
)

sealed class HomeItemCategory {
    data class PagingCategoryData(
        val title: String,
        val itemArrayList: ArrayList<SellItem>) : HomeItemCategory() //AbstractHomeData(HomeMainAdapter.COMMON_PAGING_SECTION)

    //data class HorizontalCategoryData(
        //val title: String,
      //  val itemArrayList: ArrayList<SellItemViewData>
    //) : HomeItemCategory()

    data class GridCategoryData(
        val title: String,
        val itemArrayList: ArrayList<SellItem>
    ) : HomeItemCategory()
}


/**
 * 데이터가 공통적으로 type 과 세부 어레이의 형태이기 때문에,
 * 세부 아이템만 분기를 하고 추상 메서드 상속으로 처리한 후,
 * 세부 어뎁터에 데이터를 넣어줄 때 다운캐스팅으로 아이템 데이터 처리.
 */
abstract class AbstractHomeData(
    val type: Int
)

data class PagingCategoryData(
    val title: String,
    val itemArrayList: ArrayList<SellItem>
) : AbstractHomeData(HomeMainAdapter.COMMON_PAGING_SECTION)

data class GridCategoryData(
    val title: String,
    val itemArrayList: ArrayList<SellItem>
) : AbstractHomeData(HomeMainAdapter.COMMON_GRID_SECTION)
