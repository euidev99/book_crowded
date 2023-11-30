package com.example.bookcrowded.ui.enroll

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bookcrowded.common.AuthManager
import com.example.bookcrowded.databinding.ActivityEnrollmentBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.common.BaseRepository
import com.example.bookcrowded.ui.dto.SellItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.util.UUID

class EnrollActivity : BaseActivity() {
    private var selectedImage: Bitmap? = null

    //기본 공통 세팅
    private var _binding: ActivityEnrollmentBinding? = null
    private val binding get() = _binding!!

    private val itemRepository = BaseRepository("SellItem" , SellItem::class.java)

    private val firestore = FirebaseFirestore.getInstance()
    private val itemsCollection = firestore.collection("SellItem") //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEnrollmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // editSwitch 초기 상태를 true로 설정 (판매 중 상태)
        binding.editSwitch.isChecked = true
        binding.editSwitch.text = "판매 완료"

        // 스위치 상태 변경 리스너 추가
        binding.editSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.editSwitch.text = "판매 완료"
            } else {
                binding.editSwitch.text = "판매 중"
            }
        }

        binding.enrollmentButton.setOnClickListener {

//            // 사용자 계정 ID 가져오기
//            val userId = AuthManager.userId

            // 사용자에게 입력받은 값들
            val itemName = binding.editTitle.text.toString()
            val itemPrice = binding.editPrice.text.toString()
            val userEmail = AuthManager.userEmail // 사용자 이메일은 로그인 정보에서 가져온다고 가정
            val itemDescription = binding.editDescription.text.toString()
            val isSold = binding.editSwitch.isChecked

            // 이미지를 업로드하지 않았다면 빈 문자열로 설정
            val imageUrl = ""

            // UUID를 사용하여 충돌 가능성 최소화
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

// Firestore에 데이터 추가
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

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 부여되지 않았으므로 권한 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST
            )
        } else {
            // 이미 권한 부여
            openGallery()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    // showToast 함수 추가
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun setView() {

        //addPhotoButton클릭 시 사진 등록 이벤트 처리
        binding.AddPhotoButton.setOnClickListener{
            // 갤러리를 열어 이미지를 선택하도록 함
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)

        }
        //backButton으로 디테일 화면 종료
        binding.backButton.setOnClickListener {
            finish()
        }

        // 스위치 상태 변경 리스너 추가
        binding.editSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 필요한 동작 수행
            } else {
                // 필요한 동작 수행
            }
        }


    }
    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val READ_EXTERNAL_STORAGE_REQUEST = 123
        // 액티비티를 시작하는 함수 정의
        fun startActivity(context: Context) {
            val intent = Intent(context, EnrollActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // 선택된 이미지의 URI를 가져옴
            val imageUri = data.data

            try {
                // 이미지 URI를 Bitmap으로 변환
                selectedImage = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

                // TODO: 선택된 이미지를 처리하는 로직을 구현 (예: UI에 표시)
                // 예를 들어, ImageView에 선택된 이미지를 설정할 수 있습니다:
                // binding.imageView.setImageBitmap(selectedImage)
            } catch (e: Exception) {
                Log.e("EnrollActivity", "선택된 이미지 로딩 중 오류: ${e.message}")
            }
        }
    }

}