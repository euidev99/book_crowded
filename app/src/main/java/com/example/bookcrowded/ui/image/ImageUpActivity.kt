package com.example.bookcrowded.ui.image

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil.setContentView
import com.bumptech.glide.Glide
import com.example.bookcrowded.R
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.common.AuthManager.userEmail
import com.example.bookcrowded.databinding.ActivityEnrollmentBinding
import com.example.bookcrowded.databinding.ActivityImageUpBinding
import com.example.bookcrowded.ui.chatlist.ChatListActivity
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.dto.SellItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.util.UUID

class ImageUpActivity : BaseActivity() {
    //기본 공통 세팅
    private var _binding: ActivityImageUpBinding? = null
    private val binding get() = _binding!!

    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private val itemsCollection = FirebaseFirestore.getInstance().collection("SellItem")

    private var imageUrl: String ?= null
    private lateinit var selectedImageUri: Uri

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // 사용자가 갤러리에서 이미지를 선택한 경우
            selectedImageUri = it
            // 여기에 이미지 업로드 코드 추가
            uploadImageToFirebase()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityImageUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.AddPhotoButton.setOnClickListener {
            getContent.launch("image/*")
            uploadImageToFirebase() // 이미지 업로드를 먼저 진행
        }

        binding.enrollmentButton.setOnClickListener {

        }
    }

    private fun uploadImageToFirebase(){
        val imagesRef = storageRef.child("image/${selectedImageUri.lastPathSegment}")

        imagesRef.putFile(selectedImageUri)
            .addOnSuccessListener {
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString()
                    loadAndDisplayImage(uri)
                }
        }
            .addOnFailureListener { exception ->
                showToast("이미지 업로드 실패")
            }


    }

    private fun enrollSellItem(imageUrl: String){
        val itemName = binding.editTitle.text.toString()
        val itemPrice = binding.editPrice.text.toString()
        val userEmail = AuthManager.userEmail // 사용자 이메일은 로그인 정보에서 가져온다고 가정
        val itemDescription = binding.editDescription.text.toString()
        val isSold = binding.editSwitch.isChecked

        val itemId = UUID.randomUUID().toString()
        val currentDate = LocalDate.now().toString()

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

        CoroutineScope(Dispatchers.IO).launch {
            try {
                itemsCollection.add(sellItem).await()
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