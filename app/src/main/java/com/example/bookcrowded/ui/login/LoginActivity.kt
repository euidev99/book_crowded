package com.example.bookcrowded.ui.login

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookcrowded.databinding.ActivityLoginBinding
import com.example.bookcrowded.databinding.ActivityMainBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Login 화면
 * email 과 패스워드를 사용하여 로그인
 */
class LoginActivity : BaseActivity() {
    //기본 공통 세팅
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setNavigationView()
//        setSampleFireBaseStore("test")
    }

    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }
}
