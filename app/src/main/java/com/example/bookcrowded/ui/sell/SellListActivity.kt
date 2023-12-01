package com.example.bookcrowded.ui.sell

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookcrowded.R
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.ActivityEntryBinding
import com.example.bookcrowded.databinding.ActivitySellListBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.common.RepoResult
import com.example.bookcrowded.ui.dto.SellItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SellListActivity : BaseActivity() {
    private var _binding: ActivitySellListBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySellListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserSellItems()
        setView()
    }

    private fun setAdapter(items: List<SellItem>) {
        val adapter = SellListAdapter(items)
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
    }


    //버튼 이벤트나 초기 이벤트 처리
    private fun setView() {

        binding.backButton.setOnClickListener{

        }
    }

    private fun loadUserSellItems() {
        CoroutineScope(Dispatchers.IO).launch {
            val userEmail = AuthManager.userEmail // 현재 로그인한 사용자의 이메일
            val repository = BaseRepository<SellItem>("SellItem", SellItem::class.java)
            val result = repository.getSellItemsByUserEmail(userEmail)

            if (result is RepoResult.Success) {
                val userSellItems = result.data
                runOnUiThread {
                    // 리사이클러뷰에 데이터 설정
                    setAdapter(userSellItems)
                }
            } else {
                // 에러 처리
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }
}
