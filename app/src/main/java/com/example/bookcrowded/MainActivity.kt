package com.example.bookcrowded

import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookcrowded.databinding.ActivityMainBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.home.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Main 홈 화면
 * 하단 네비게이션 뷰를 사용하도록 하며,
 * 안드로이드 기본 제공 기능을 응용함
 */
class MainActivity : BaseActivity() {
    //기본 공통 세팅
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigationView()
//        setSampleFireBaseStore(
//            HomeViewModel.UserInfo(
//            1111,
//            "aa",
//            "aa",
//            "aa",
//            "bb"
//        ))
    }

//    private fun setSampleFireBaseStore(data : HomeViewModel.UserInfo) {
//        FirebaseFirestore.getInstance()
//            .collection("TestCollection") //대분류
//            .document("aa")// 실 데이터
//            .set(data)//필드 값
//            .addOnSuccessListener {
//                Log.d("aaaaa", "bbbbb")
//            }
//            .addOnFailureListener {
//                Log.d("aaaaa", "ccccc")
//            }
//    }

    private fun setNavigationView() {
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        /**
         * 앱 전체 구조가 바뀌게 될 경우에 대한 테스팅
         * 특정 화면에서 하단 네비게이션 바를 없애고 전체화면을 다루기 위한 리스너 샘플
         */
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if(destination.id == R.id.navigation_search) {
//                mBinding.navView.visibility = View.GONE
//            } else {
//                mBinding.navView.visibility = View.VISIBLE
//            }
//        }

        navView.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }
}