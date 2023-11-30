package com.example.bookcrowded.ui.modi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookcrowded.databinding.ActivityModificationBinding
import com.example.bookcrowded.ui.detail.DetailActivity
import com.example.bookcrowded.ui.dto.SellItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ModificationActivity : AppCompatActivity() {

    private lateinit var itemId: String
    private lateinit var sellItem: SellItem
    private lateinit var binding: ActivityModificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로 전달된 ITEM_ID를 받아옴
        itemId = intent.getStringExtra("ITEM_ID") ?: ""

        // Firestore에서 해당 판매글 정보를 가져와 화면에 표시
        val firestore = FirebaseFirestore.getInstance()
        val itemsCollection = firestore.collection("SellItem")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val document = itemsCollection.document(itemId).get().await()
                sellItem = document.toObject(SellItem::class.java)!!

                // UI에 판매글 정보 표시
                runOnUiThread {
                    binding.editTitle.setText(sellItem.title)
                    binding.editPrice.setText(sellItem.price)
                    binding.editDescription.setText(sellItem.description)
                    binding.editSwitch.isChecked = sellItem.sold
                }
            } catch (e: Exception) {
                Log.e("ModificationActivity", "판매글 정보 가져오기 오류: ${e.message}")
            }
        }

        // 수정 완료 버튼 리스너 설정
        binding.modificationButton.setOnClickListener {
            // 수정된 정보를 사용하여 Firestore 업데이트
            if (binding.editTitle.text != null) {
                // 판매글 수정 로직 구현
            }
            updateSellItem()
        }

        // backButton 클릭 이벤트 핸들러
        binding.backButton.setOnClickListener {
            // 뒤로가기 버튼을 눌렀을 때의 동작 구현
            navigateToDetailActivity()
        }
    }

    private fun updateSellItem() {
        sellItem.title = binding.editTitle.text.toString()
        sellItem.price = binding.editPrice.text.toString()
        sellItem.description = binding.editDescription.text.toString()
        sellItem.sold = binding.editSwitch.isChecked

        // 여기서 Firestore 업데이트 로직을 추가하면 됩니다.
        // 예를 들어, 아래와 같이 업데이트할 수 있습니다.
        // itemsCollection.document(itemId).set(sellItem).await()

        showToast("게시물 수정 성공")
        navigateToDetailActivity()
    }

    private fun navigateToDetailActivity() {
        DetailActivity.startActivityWithItemId(this, itemId)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        // 액티비티를 시작하는 함수 정의
        private const val ITEM_ID = "id"
        fun startActivity(context: Context) {
            val intent = Intent(context, ModificationActivity::class.java)
            context.startActivity(intent)
        }

        fun startActivityWithItemId(context: Context, itemId: String) {
            val intent = Intent(context, ModificationActivity::class.java)
            intent.putExtra(ITEM_ID, itemId)
            context.startActivity(intent)
        }
    }
}
