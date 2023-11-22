package com.example.bookcrowded.ui.entry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bookcrowded.R
import com.example.bookcrowded.databinding.ActivityEntryBinding
import com.example.bookcrowded.databinding.ActivityRegisterBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.login.LoginActivity
import com.example.bookcrowded.ui.regist.RegisterActivity

/**
 * Entry 화면
 * login / register 분기
 */

class EntryActivity : BaseActivity() {
    private var _binding: ActivityEntryBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
    }

    private fun setView() {
        //회원가입 화면으로 이동
        binding.entryRegisterButton.setOnClickListener {
            RegisterActivity.startActivity(this)
        }

        //로그인 화면으로 이동
        binding.entryLoginButton.setOnClickListener {
            LoginActivity.startActivity(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }
}