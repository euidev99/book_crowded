package com.example.bookcrowded.ui.modi

import android.os.Bundle
import com.example.bookcrowded.databinding.ActivityModificationBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.dto.SellItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModificationActivity : BaseActivity() {
    //기본 공통 세팅
    private var _binding: ActivityModificationBinding? = null
    private val binding get() = _binding!!

    private val itemRepository = BaseRepository("SellItem" , SellItem::class.java)
    private var itemId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityModificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 수정하려는 상품의 아이템 ID를 받아옴
        itemId = intent.getStringExtra("ITEM_ID")

        // 가져오기(수정하기 전에 기존 데이터를 표시하기 위해)
        itemId?.let { getItemDetails(it) }

        // 수정 버튼 클릭 시
        binding.modificationButton.setOnClickListener {
            itemId?.let { updateItem(it) }
        }

        setView()
    }

//        binding.modificationButton.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                when (val result = itemRepository.addDocument(
//                    SellItem(AuthManager.userId,
//                        "상품명_테스트",
//                        "9999",
//                        AuthManager.userEmail,
//                        "상품 설명",
//                        "",
//                        false,
//                        "2023-11-11",
//                        false))
//                ) {
//                    is RepoResult.Success -> {
//                        finish()
//                    }
//                    is RepoResult.Error -> {
//
//                    }
//                }
//            }
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
    private fun getItemDetails(itemId: String) {
    CoroutineScope(Dispatchers.IO).launch {
//        when (val result = itemRepository.getDocument(itemId)) {
//            is RepoResult.Success -> {
//                val sellItem = result.data
//                // 여기에서 가져온 데이터를 사용하여 화면에 표시하도록 처리
//                // 예: binding.titleEditText.setText(sellItem.title)
//                //     binding.descriptionEditText.setText(sellItem.description)
//                //     등등
//            }
//            is RepoResult.Error -> {
//                // 에러 처리
//            }
        }
    }
//}

    private fun updateItem(itemId: String) {
        CoroutineScope(Dispatchers.IO).launch {
//        when (val result = itemRepository.getDocument(itemId)) {
//            is RepoResult.Success -> {
//                val sellItem = result.data
//                // 여기에서 수정할 내용을 sellItem에 적용
//                // 예: sellItem.title = binding.titleEditText.text.toString()
//                //     sellItem.description = binding.descriptionEditText.text.toString()
//                //     등등
//
//                // 수정된 내용을 업데이트
//                when (val updateResult = itemRepository.updateDocument(itemId, sellItem)) {
//                    is RepoResult.Success -> {
//                        finish()
//                    }
//                    is RepoResult.Error -> {
//                        // 업데이트 에러 처리
//                    }
//                }
//            }
//            is RepoResult.Error -> {
//                // 가져오기 에러 처리
//            }
        }
    }
//}




    private fun setView() {
        //backButton으로 디테일 화면 종료
//        binding.backButton.setOnClickListener {
//            finish()
//        }

    }
//    override fun onDestroy() {
//        super.onDestroy()
//        this._binding = null
//    }

//    companion object {
//        // 액티비티를 시작하는 함수 정의
//        fun startActivity(context: Context) {
//            val intent = Intent(context, ModificationActivity::class.java)
//            context.startActivity(intent)
//        }
//    }
//}