package com.example.bookcrowded.ui.my

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.FragmentMyBinding
import com.example.bookcrowded.ui.chatlist.ChatListActivity
import com.example.bookcrowded.ui.common.BaseFragment
import com.example.bookcrowded.ui.image.ImageUpActivity
import com.example.bookcrowded.ui.entry.EntryActivity
import com.google.firebase.auth.FirebaseAuth

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
        setView()
    }

//    private fun setView() {
//        binding.chatLayout.setOnClickListener {
//            context?.let { it1 -> ChatListActivity.startActivity(it1) }
//        }
//    }

    private fun setView() {
        binding.logoutButton.setOnClickListener {
            // Firebase 로그아웃 처리
            FirebaseAuth.getInstance().signOut()

            // AuthManager의 사용자 정보 초기화
            AuthManager.userEmail = ""
            AuthManager.userPassword = ""

            // 로그아웃 후 EntryActivity로 이동
            navigateToEntryActivity()
        }

        binding.ChatLayout.setOnClickListener {
            // 현재 Context를 가져와서 startActivity 호출
            val context = getContext()
            context?.let { ChatListActivity.startActivity(it) }
        }

        binding.ImageLayout.setOnClickListener {
            // 현재 Context를 가져와서 startActivity 호출
            val context = getContext()
            context?.let { ImageUpActivity.startActivity(it) }
        }

    }
    private fun navigateToEntryActivity() {
        // EntryActivity로 이동하는 인텐트 생성
        val intent = Intent(requireContext(), EntryActivity::class.java)
        // 모든 이전 액티비티를 클리어하고 새로 시작
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        // 현재 액티비티 종료
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}