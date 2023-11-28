package com.example.bookcrowded.ui.enroll

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.ActivityEnrollmentBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.dto.SellItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EnrollActivity : BaseActivity() {
    //기본 공통 세팅
    private var _binding: ActivityEnrollmentBinding? = null
    private val binding get() = _binding!!

    private val itemRepository = BaseRepository("SellItem" , SellItem::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEnrollmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // editSwitch 초기 상태를 true로 설정 (판매 중 상태)
        binding.editSwitch.isChecked = true
        binding.editSwitch.text = "판매 중"

        // 스위치 상태 변경 리스너 추가
        binding.editSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 판매 중
                binding.editSwitch.text = "판매 중"
            } else {
                // 판매 완료
                binding.editSwitch.text = "판매 완료"
            }
        }

        binding.enrollmentButton.setOnClickListener {
            val isSold = binding.editSwitch.isChecked

            // 사용자 계정 ID 가져오기
            val userId = AuthManager.userId

            // 스위치 상태에 따라 sold 속성 설정
            CoroutineScope(Dispatchers.IO).launch {
                when (val result = itemRepository.addDocument(
                    SellItem(AuthManager.userId,
                        "hihi",
                        "9999",
                        AuthManager.userEmail,
                        "상품 설명",
                        "",
                        false,
                        "2023-11-11",
                        false
                    )
                )) {
                    is RepoResult.Success -> {
                        // 등록 성공
                        runOnUiThread {
                            showToast("게시물 등록 성공")
                        }
                        finish()
                    }
                    is RepoResult.Error -> {
                        // 등록 실패
                        runOnUiThread {
                            showToast("게시물 등록 실패")
                        }
                    }
                }
            }
        }
        /*binding.addSampleItemSold.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                itemRepository.addDocument(
                    SellItem(AuthManager.userId,
                        "팔린 상품",
                        "1111",
                        AuthManager.userEmail,
                        "팔렸지롱",
                        "",
                        false,
                        "2023-11-11",
                        true))
            }
        }*/

        setView()

//        binding.addSampleItemFavorite.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                itemRepository.addDocument(
//                    SellItem(AuthManager.userId,
//                        "찜 상품",
//                        "1111",
//                        AuthManager.userEmail,
//                        "찜이지롱",
//                        "",
//                        false,
//                        "2023-11-11",
//                        true))
//            }
//        }
    }

    // showToast 함수 추가
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun setView() {
        //backButton으로 디테일 화면 종료
        binding.backButton.setOnClickListener {
            finish()
        }

        // 스위치 상태 변경 리스너 추가
        binding.editSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 판매 중
                // 필요한 동작 수행
            } else {
                // 판매 완료
                // 필요한 동작 수행
            }
        }


    }
    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }

    companion object {
        // 액티비티를 시작하는 함수 정의
        fun startActivity(context: Context) {
            val intent = Intent(context, EnrollActivity::class.java)
            context.startActivity(intent)
        }
    }
}