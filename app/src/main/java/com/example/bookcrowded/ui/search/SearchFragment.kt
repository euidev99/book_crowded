package com.example.bookcrowded.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookcrowded.R
import com.example.bookcrowded.databinding.FragmentHomeBinding
import com.example.bookcrowded.databinding.FragmentMyBinding
import com.example.bookcrowded.databinding.FragmentSearchBinding
import com.example.bookcrowded.ui.common.BaseFragment
import com.example.bookcrowded.ui.detail.DetailActivity
import com.example.bookcrowded.ui.dto.SellItem
import com.example.bookcrowded.ui.my.MyViewModel

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
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        setView()

        mViewModel.itemList.observe(viewLifecycleOwner) { setAdapter(it) }

        return binding.root
    }

    private fun setView() {
        binding.backButton.setOnClickListener {
            //
        }

        binding.optionButton.setOnClickListener {

        }
    }

    private fun setAdapter(itemList: List<SellItem>) {
        Log.d("aa",  "asdasda " + itemList.size)
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