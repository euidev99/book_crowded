package com.example.bookcrowded.ui.image

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.bookcrowded.common.AppConst
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.ActivityImageUpBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.dto.SellItem
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class ImageUpActivity : BaseActivity() {
    //기본 공통 세팅
    private var _binding: ActivityImageUpBinding? = null
    private val binding get() = _binding!!

    //storage
    private val storage = Firebase.storage
    private val storageRef = storage.reference
//    private val itemsCollection = FirebaseFirestore.getInstance().collection("SellItem")

    private val itemsRepository = BaseRepository(AppConst.FIREBASE.SELL_ITEM, SellItem::class.java)

    private var imageUrl: String = ""
    private lateinit var selectedImageUri: Uri
    private var imageSelected = false

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            showProgressUI()
            //이미지를 선택한 경우
            selectedImageUri = it
            //뷰에 이미지 추가
            imageSelected = true
            loadAndDisplayImage(it)
            hideProgressUI()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityImageUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

        //backButton으로 디테일 화면 종료
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.AddPhotoButton.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.enrollmentButton.setOnClickListener {
            enrollSellItem()
        }

    }

    private fun enrollSellItem(){
        val itemName = binding.editTitle.text.toString()
        val itemPrice = binding.editPrice.text.toString()
        val userEmail = AuthManager.userEmail // 사용자 이메일은 로그인 정보에서 가져온다고 가정
        val itemDescription = binding.editDescription.text.toString()
        val isSold = binding.editSwitch.isChecked

        val itemId = UUID.randomUUID().toString()
        val currentDate = LocalDate.now().toString()

        //내용 없으면 리턴
        if (itemName == "" || itemPrice == "" || itemDescription == "") {
            return
        }

        val sellItem = SellItem(
            id = itemId,
            title = itemName,
            price = itemPrice,
            sellerEmail = userEmail,
            description = itemDescription,
            image = imageUrl,
            favorite = false,
            upLoadDate = currentDate,
            sold = isSold
        )


        if (imageSelected) {
            val imagesRef = storageRef.child("image/${selectedImageUri.lastPathSegment}")

            imagesRef.putFile(selectedImageUri)
                .addOnSuccessListener {
                    imagesRef.downloadUrl.addOnSuccessListener { uri ->
                        showToast("이미지 등록 성공")
                    }
                }
                .addOnFailureListener { exception ->
                    showToast("이미지 업로드 실패")
                }
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                itemsRepository.addDocument(sellItem)
                runOnUiThread {
                    showToast("게시물 등록 성공")
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    showToast("게시물 등록 실패")
                    Log.e("EnrollActivity", "Firestore에 데이터 추가 중 오류: ${e.message}")
                }
            }
        }
    }

    private fun loadAndDisplayImage(imageUri: Uri){
        Glide.with(this)
            .load(imageUri)
            .into(binding.AddedPhoto)

        imageUrl = selectedImageUri.lastPathSegment.toString()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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