package com.example.bookcrowded.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookcrowded.databinding.FragmentHomeBinding
import com.example.bookcrowded.ui.common.BaseFragment
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.listener.OnRecyclerViewItemClickListener
import com.example.bookcrowded.ui.home.viewdata.HomeViewData
import com.example.bookcrowded.ui.test.TestActivity
import kotlinx.coroutines.coroutineScope

class HomeFragment : BaseFragment() {
    private var _binding :FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.publicHomeData.observe(viewLifecycleOwner) { setAdapter(it) } //데이터 옵저빙
        homeViewModel.getItemList()
        //homeViewModel.getAllCategory()
        //homeViewModel.getAllUsers()
    }

    private fun setAdapter(data: HomeViewData) {
        binding.recycler.apply {
            adapter = HomeMainAdapter(object: OnRecyclerViewItemClickListener {
                override fun onClick(v: View, position: Int) {
                    Log.d("Seki", "MainClick $position View: ${v.tag}")
                }

                override fun onSubItemClick(v: View, position: Int) {
                    Log.d("Seki", "SubListClick $position View: ${v.tag}")
                    //TestActivity.startActivity(context)
                }

            }).build(data.itemData)

            layoutManager = LinearLayoutManager(this@HomeFragment.context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}