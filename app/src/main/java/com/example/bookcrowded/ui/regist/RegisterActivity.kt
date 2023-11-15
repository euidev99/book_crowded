package com.example.bookcrowded.ui.regist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.bookcrowded.databinding.ActivityRegisterBinding
import com.example.bookcrowded.ui.common.BaseActivity

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
            } else {
                // 가입 실패 시의 처리
                Toast.makeText(this, "가입 실패", Toast.LENGTH_SHORT).show()
            }
        }
        setView()
    }

    private fun setView() {
        //회원가입 버튼
        binding.registerButton.setOnClickListener {
        //회원가입 화면으로 이동
        RegisterActivity.startActivity(this)
        }

        //회원가입하기
        binding.registerButton.setOnClickListener {
            val id: String = binding.idEditText.text.toString()
            val email: String = binding.emailEditText.text.toString()
            val passwd: String = binding.passwdEditText.text.toString()
            val name: String = binding.nameEditText.text.toString()
            val birth: Int = (binding.birthEditText.text.toString()).toInt() //예외처리 필요함 근데 귀찮음

            registerViewModel.register(id, email, passwd, name, birth)
        }

        binding.checkDuplicateButton.setOnClickListener {
            val email: String = binding.emailEditText.text.toString()
            registerViewModel.checkDuplicated(email)
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
