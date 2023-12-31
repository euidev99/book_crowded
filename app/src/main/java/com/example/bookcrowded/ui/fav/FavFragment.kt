package com.example.bookcrowded.ui.fav

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookcrowded.databinding.FragmentFavBinding
import com.example.bookcrowded.ui.common.BaseFragment
import com.example.bookcrowded.ui.dto.SellItem


class FavFragment : BaseFragment() {
    private var _binding: FragmentFavBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: FavViewModel

    private var isSet = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("fav", "Fav view")
        _binding = FragmentFavBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(this)[FavViewModel::class.java]
        mViewModel.progressListener = this

        mViewModel.favoriteItemList.observe(viewLifecycleOwner) { setAdapter(it) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.getFavoriteItemList()
    }

    private fun setAdapter(itemList: List<SellItem>) {
        if (isSet) {
            (binding.recycler.adapter as FavVerticalAdapter).submitChatMessages(itemList)
            binding.recycler.smoothScrollToPosition(itemList.size - 1)
        } else {
            isSet = true
            binding.recycler.apply {
                adapter = FavVerticalAdapter(
                    items = itemList,
                    itemClickListener = object : FavVerticalAdapter.OnItemClickListener {
                        override fun onClick(v: View, position: Int) {
                            // Handle item click
                        }
                    }
                )

                layoutManager = LinearLayoutManager(this@FavFragment.context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }
}