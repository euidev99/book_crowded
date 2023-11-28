package com.example.bookcrowded.ui.image

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookcrowded.R
import com.example.bookcrowded.databinding.ActivityEnrollmentBinding
import com.example.bookcrowded.databinding.ActivityImageUpBinding
import com.example.bookcrowded.ui.chatlist.ChatListActivity

class ImageUpActivity : AppCompatActivity() {
    //기본 공통 세팅
    private var _binding: ActivityImageUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityImageUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        // 액티비티를 시작하는 함수 정의
        fun startActivity(context: Context) {
            val intent = Intent(context, ImageUpActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }
}