package com.example.bookcrowded.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.bookcrowded.R
import com.example.bookcrowded.databinding.FragmentHomeBinding
import com.example.bookcrowded.databinding.FragmentMyBinding
import com.example.bookcrowded.databinding.FragmentSearchBinding
import com.example.bookcrowded.ui.common.BaseFragment
import com.example.bookcrowded.ui.my.MyViewModel

class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!
//    private val mViewModel: SearchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //여기에 작업
    }

}