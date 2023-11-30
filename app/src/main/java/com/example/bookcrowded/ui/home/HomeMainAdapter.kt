package com.example.bookcrowded.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.example.bookcrowded.databinding.ItemGridSectionHolderBinding
import com.example.bookcrowded.databinding.ItemPagingSectionHolderBinding
import com.example.bookcrowded.ui.common.listener.OnRecyclerViewItemClickListener
import com.example.bookcrowded.ui.home.viewdata.HomeItemCategory
import com.google.android.material.tabs.TabLayoutMediator

/**
 * OnDemand 에서의 메인 세로 어댑터
 * 각각 세부 어댑터들 뷰홀더로 분기하여 세팅
 */
class HomeMainAdapter(private var itemClickListener: OnRecyclerViewItemClickListener) : RecyclerView.Adapter<ViewHolder>() {
    /**
     * 아이템 타입 지정자로, 리사이클러뷰 내부에서
     * 각 뷰를 구분하기 위한 목적으로 데이터 클래스 내부에 지정해서 사용
     */
    companion object {
        const val INVALIDATE = -1
        const val COMMON_PAGING_SECTION = 0 // 최상단 페이징 섹션
        const val COMMON_GRID_SECTION = 1 // 그리드 아이템 섹션
    }

    //메인 데이터 셋
    private lateinit var dataList: ArrayList<HomeItemCategory>

    //데이터 셋
    fun build(i: ArrayList<HomeItemCategory>): HomeMainAdapter {
        dataList = i
        return this
    }

    /**
     * 아이템 타입 지정자
     */
    override fun getItemViewType(position: Int): Int {
        return when (dataList[position]) {
            is HomeItemCategory.PagingCategoryData -> COMMON_PAGING_SECTION
            is HomeItemCategory.GridCategoryData -> COMMON_GRID_SECTION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            COMMON_PAGING_SECTION -> {
                HorizontalPagingViewHolder(
                    ItemPagingSectionHolderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    parent.context,
                    this.itemClickListener
                )
            }

            COMMON_GRID_SECTION -> {
                GridViewHolder(
                    ItemGridSectionHolderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    parent.context,
                    this.itemClickListener
                )
            }

            else -> { //그냥 아무거나..나중에 처리 필요
                HorizontalPagingViewHolder(
                    ItemPagingSectionHolderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    parent.context,
                    this.itemClickListener
                )
            }
        }
    }


    /**
     * 내부 아이템 뷰홀더이며, 뷰홀더 내부에서의 리사이클러뷰 클릭 이벤트를
     * 메인 리스너로 전달하여 액티비티에서 전부 처리할 수 있도록 함
     */
    class HorizontalPagingViewHolder(
        private val binding: ItemPagingSectionHolderBinding,
        val context: Context,
        val listener: OnRecyclerViewItemClickListener
    ) :
        ViewHolder(binding.root) {

        fun bind(item: HomeItemCategory.PagingCategoryData) {
            with(binding) {
                titleText.text = item.title
                //어댑터 혹은 셀 세팅
                holderPager.apply {
                    adapter = PagingItemAdapter(item.itemArrayList, object :
                        PagingItemAdapter.OnItemClickListener {
                        override fun onClick(v: View, position: Int) {
                            listener.onSubItemClick(v, position)
                        }
                    })

                    orientation = ViewPager2.ORIENTATION_HORIZONTAL
                }

                TabLayoutMediator(
                    pagingTab,
                    holderPager
                ) { tab, position ->
                    holderPager.currentItem = tab.position
                }.attach()
            }
        }
    }

    /**
     * 내부 아이템 뷰홀더이며, 뷰홀더 내부에서의 리사이클러뷰 클릭 이벤트를
     * 메인 리스너로 전달하여 액티비티에서 전부 처리할 수 있도록 함
     */
    class GridViewHolder(
        private val binding: ItemGridSectionHolderBinding,
        val context: Context,
        val listener: OnRecyclerViewItemClickListener
    ) : ViewHolder(binding.root) {
        fun bind(item: HomeItemCategory.GridCategoryData) {
            with(binding) {
                recyclerGrid.apply {
                    adapter = GridItemAdapter(
                        items = item.itemArrayList,
                        itemClickListener = object : GridItemAdapter.OnItemClickListener {
                            override fun onClick(v: View, position: Int) {
                                listener.onSubItemClick(v, position)
                            }
                        }
                    )

                    layoutManager = GridLayoutManager(context, 3) // 3개의 열을 가진 그리드 레이아웃을 사용
                }
            }
        }
    }

    /**
     * 첫번째 메인 리사이클러뷰에 대한 뷰홀더에 대한 인터페이스 지정,
     * 뷰 홀더 내부에서 호출되는 bind 함수에
     * 리스너까지 생성자에 전달해도 좋은데 임시로 이렇게 사용
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = dataList[position]) {
            is HomeItemCategory.PagingCategoryData -> {
                (holder as? HorizontalPagingViewHolder)?.bind(item)
            }
            is HomeItemCategory.GridCategoryData -> {
                (holder as? GridViewHolder)?.bind(item)
            }
        }
    }

    override fun getItemCount(): Int = dataList.count()
}