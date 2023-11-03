package com.example.bookcrowded.ui.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.bookcrowded.R
import com.example.bookcrowded.databinding.FragmentHomeBinding
import com.example.bookcrowded.databinding.FragmentMyBinding
import com.example.bookcrowded.ui.common.BaseFragment

class MyFragment : BaseFragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //My 페이지 관련 세팅
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}