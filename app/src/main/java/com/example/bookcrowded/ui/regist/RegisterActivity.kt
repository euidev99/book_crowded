package com.example.bookcrowded.ui.regist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import com.example.bookcrowded.MainActivity
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.ActivityRegisterBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.entry.EntryActivity
import com.example.bookcrowded.ui.login.LoginActivity

/**
 * 계정 등록 화면
 */
class RegisterActivity : BaseActivity() {
    //기본 공통 세팅
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel.progressListener = this
        registerViewModel.duplicatedResult.observe(this) { duplicated ->
            if (duplicated) {
                // 중복 체크 시의 처리
                Toast.makeText(this, "중복 Email", Toast.LENGTH_SHORT).show()
            } else {
                // 중복 아닐 시의 처리
                Toast.makeText(this, "없는 Email", Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.registerResult.observe(this) { success ->
            if (success) {
                // 가입 성공 시의 처리
                Toast.makeText(this, "가입 성공!", Toast.LENGTH_SHORT).show()
//                AuthManager.userEmail = binding.emailEditText.text.toString()
//                AuthManager.userPassword = binding.passwdEditText.text.toString()
//                MainActivity.startActivity(this)
                finish()

            } else {
                // 가입 실패 시의 처리
                Toast.makeText(this, "가입 실패", Toast.LENGTH_SHORT).show()
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

    private fun setView() {
        //회원가입하기
        binding.registerButton.setOnClickListener {
            val email: String = binding.emailEditText.text.toString()
            val passwd: String = binding.passwdEditText.text.toString()
            val name: String = binding.nameEditText.text.toString()
            val birth: String = (binding.birthEditText.text.toString())

            registerViewModel.register(email, passwd, name, birth)
        }

        binding.checkDuplicateButton.setOnClickListener {
            val email: String = binding.emailEditText.text.toString()
            registerViewModel.checkDuplicated(email)
        }

        binding.registerBackButton.setOnClickListener {
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
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}
