package com.example.bookcrowded.ui.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.bookcrowded.MainActivity
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.ActivityLoginBinding
import com.example.bookcrowded.databinding.ActivityTestBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.dto.SellItem
import com.example.bookcrowded.ui.regist.RegisterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 테스트 화면
 */
class TestActivity : BaseActivity() {
    //기본 공통 세팅
    private var _binding: ActivityTestBinding? = null
    private val binding get() = _binding!!

    private val itemRepository = BaseRepository("SellItem" , SellItem::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addSampleItemButton.setOnClickListener {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }

    companion object {
        // 액티비티를 시작하는 함수 정의
        fun startActivity(context: Context) {
            val intent = Intent(context, TestActivity::class.java)
            context.startActivity(intent)
        }
    }
}
