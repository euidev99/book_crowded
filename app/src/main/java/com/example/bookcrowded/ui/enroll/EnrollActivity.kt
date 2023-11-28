package com.example.bookcrowded.ui.enroll

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.ActivityEnrollmentBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.common.BaseRepository
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

        binding.enrollmentButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                itemRepository.addDocument(
                    SellItem(AuthManager.userId,
                        "상품명_테스트",
                        "9999",
                        AuthManager.userEmail,
                        "상품 설명",
                        "",
                        false,
                        "2023-11-11",
                        false))
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


    private fun setView() {
        //backButton으로 디테일 화면 종료
        binding.backButton.setOnClickListener {
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
            val intent = Intent(context, EnrollActivity::class.java)
            context.startActivity(intent)
        }
    }
}