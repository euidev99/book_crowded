package com.example.bookcrowded.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookcrowded.MainActivity
import com.example.bookcrowded.R
import com.example.bookcrowded.databinding.ActivityMainBinding
import com.example.bookcrowded.databinding.ActivitySplashBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.entry.EntryActivity
import com.example.bookcrowded.ui.login.LoginActivity
import java.util.Timer
import java.util.TimerTask

class SplashActivity : BaseActivity() {
    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!
    private val SPLASH_TIME_OUT: Long = 2000 // 2초

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTimer()
    }

    private fun setTimer() {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val intent = Intent(this@SplashActivity, EntryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, SPLASH_TIME_OUT)
    }
}