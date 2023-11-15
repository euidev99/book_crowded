package com.example.bookcrowded.ui.search

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListPopupWindow
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookcrowded.databinding.FragmentSearchBinding
import com.example.bookcrowded.ui.common.BaseFragment
import com.example.bookcrowded.ui.detail.DetailActivity
import com.example.bookcrowded.ui.dto.SellItem
import com.laundrycrew.delivery.order.common.CustomPopupListAdapter


class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!
    private lateinit var mViewModel: SearchViewModel

    private var isSet = false;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("aaa", "when Create View?")
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        mViewModel.progressListener = this

        setView()

        mViewModel.itemList.observe(viewLifecycleOwner) { setAdapter(it) }

        return binding.root
    }

    private fun setView() {

        binding.optionButton.setOnClickListener {
            showCustomPopupListView(it)
            //검색 필터 적용할 때 쓰면 되는 코드
//            mViewModel.getSoldItemList(false)
//            mViewModel.getSoldItemList(true)
//            mViewModel.getItemList()
        }
    }

    private fun showCustomPopupListView(anchorView: View) {
        //아이템
        val popupList: MutableList<String> = ArrayList()
        popupList.add("전체 보기")
        popupList.add("판매 완료 보기")
        popupList.add("안팔린 상품 보기")
        val adapter = CustomPopupListAdapter(requireContext(), popupList)
        val listPopupWindow = ListPopupWindow(requireContext(), null, 0, com.example.bookcrowded.R.style.CustomListPopupWindowStyle)

        listPopupWindow.setAdapter(adapter)
        listPopupWindow.anchorView = anchorView
        listPopupWindow.setDropDownGravity(Gravity.END)
        listPopupWindow.width = 400
        listPopupWindow.height = ListPopupWindow.WRAP_CONTENT
        listPopupWindow.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> {
                    mViewModel.getItemList()
                }
                1 -> {
                    mViewModel.getSoldItemList(true)
                }
                2 -> {
                    mViewModel.getSoldItemList(false)
                }
            }
            //동작 처리
            listPopupWindow.dismiss()
        })
        listPopupWindow.show()
    }

    private fun setAdapter(itemList: List<SellItem>) {
        if (isSet) {
            (binding.recycler.adapter as SearchVerticalAdapter).submitChatMessages(itemList)
            binding.recycler.smoothScrollToPosition(itemList.size - 1)
        } else {
            isSet = true
            binding.recycler.apply {
                adapter =
                    SearchVerticalAdapter(
                        items = itemList,
                        itemClickListener = (object :
                            SearchVerticalAdapter.OnItemClickListener {

                            override fun onClick(v: View, position: Int) {
                                //ItemClick
                                DetailActivity.startActivityWithItemId(context, mViewModel.getItemIdByPosition(position))
                            }
                        })
                    )
                layoutManager = LinearLayoutManager (context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //여기에 작업
        mViewModel.getItemList()
    }
}