package com.example.bookcrowded.ui.regist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.bookcrowded.databinding.ActivityRegisterBinding
import com.example.bookcrowded.ui.common.BaseActivity

/**
 * 계정 등록 화면
 */
class RegisterActivity : BaseActivity() {
    //기본 공통 세팅
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

//    private val RegistViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        loginViewModel.progressListener = this
//        loginViewModel.loginResult.observe(this) { success ->
//            if (success) {
//                // 로그인 성공 시의 처리
//                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
//                AuthManager.userId = binding.emailEditText.text.toString()
//                MainActivity.startActivity(this)
//            } else {
//                // 로그인 실패 시의 처리
//                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        binding.loginButton.setOnClickListener {
//            //test aa, aa
//            val email: String = binding.emailEditText.text.toString()
//            val passwd: String = binding.passwdEditText.text.toString()
//
//            loginViewModel.logIn(email, passwd)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }

    companion object {
        // 액티비티를 시작하는 함수 정의
        fun startActivity(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}
