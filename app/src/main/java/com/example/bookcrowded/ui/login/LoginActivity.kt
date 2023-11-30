package com.example.bookcrowded.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import com.example.bookcrowded.MainActivity
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.ActivityLoginBinding
import com.example.bookcrowded.ui.common.BaseActivity


/**
 * Login 화면
 * email 과 패스워드를 사용하여 로그인
 */
class LoginActivity : BaseActivity() {
    //기본 공통 세팅
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.progressListener = this
        loginViewModel.loginResult.observe(this) { success ->
            if (success) {
                // 로그인 성공 시의 처리
                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                AuthManager.userEmail = binding.emailEditText.text.toString()
                AuthManager.userPassword = binding.passwdEditText.text.toString()
                MainActivity.startActivity(this)
                finish()
            } else {
                // 로그인 실패 시의 처리
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }

        binding.root.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }

        setView()
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = currentFocus
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    //버튼 이벤트나 초기 이벤트 처리
    private fun setView() {
        //로그인 버튼
        binding.loginButton.setOnClickListener {
            //test aa, aa
            val email: String = binding.emailEditText.text.toString()
            val passwd: String = binding.passwdEditText.text.toString()

            loginViewModel.logIn(email, passwd)
        }

        binding.loginBackButton.setOnClickListener {
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }

    companion object {
        // 액티비티를 시작하는 함수 정의
        fun startActivity(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}
