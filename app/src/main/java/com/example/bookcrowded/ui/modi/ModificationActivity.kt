package com.example.bookcrowded.ui.modi

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bookcrowded.databinding.ActivityModificationBinding
import com.example.bookcrowded.ui.common.BaseActivity
import com.example.bookcrowded.ui.dto.SellItem

class ModificationActivity : BaseActivity() {

    //기본 공통 세팅
    private var _binding: ActivityModificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemId: String
    private lateinit var sellItem: SellItem

    private val mViewModel: ModificationViewModel by viewModels()

    private var imageUrl: String = ""
    private lateinit var selectedImageUri: Uri

    private var imageChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityModificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로 전달된 ITEM_ID를 받아옴
        itemId = intent.getStringExtra(ITEM_ID) ?: ""

        mViewModel.progressListener = this

        mViewModel.itemResult.observe(this) {
            setViewDataFromData(it)
            mViewModel.getImageFromStorageByImageUrl(it.image)
        }

        mViewModel.documentIdResult.observe(this) {

            val title = binding.editTitle.text.toString()
            val price = binding.editPrice.text.toString()
            val description = binding.editDescription.text.toString()
            val sold = binding.editSwitch.isChecked

            //내용 없으면 리턴
            if (title == "" || price == "" || description == "") {
                //nothing
            } else {

                val updates = hashMapOf(
                    "title" to title,
                    "description" to description,
                    "sold" to sold,
                    "price" to price,
                    "image" to this.imageUrl
                )

                mViewModel.updateItemByDocumentId(it, updates)
            }
        }

        mViewModel.uriResult.observe(this) {
            selectedImageUri = it

            Glide.with(this)
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(binding.AddedPhoto)

        }

        mViewModel.updateResult.observe(this) {
            if (it) {
                showToast("수정에 성공했습니다.")
                finish()
            } else {
                showToast("수정에 실패했습니다.")
            }
        }

        mViewModel.enrollImageResult.observe(this) {
            if (it) {
                showToast("이미지 업로드에 성공했습니다.")
                //아이템 로드
                intent.getStringExtra(ITEM_ID)?.let {
                    mViewModel.getDocumentIdById(it)
                }
            } else {
                showToast("이미지 업로드에 실패했습니다.")
            }
        }

        //아이템 로드
        intent.getStringExtra(ITEM_ID)?.let {
            mViewModel.getSellItemById(it)
        }


        setView()
    }

    private fun setView() {

        //backButton으로 디테일 화면 종료
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.AddPhotoButton.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.modificationButton.setOnClickListener {
            if (imageChanged) {
                mViewModel.enrollImageByUri(selectedImageUri)
            } else {
                intent.getStringExtra(ITEM_ID)?.let {
                    mViewModel.getDocumentIdById(it)
                }
            }
        }
    }

    private fun setViewDataFromData(data: SellItem) {
        binding.editTitle.setText(data.title)
        binding.editPrice.setText(data.price)
        binding.editDescription.setText(data.description)
        binding.editSwitch.isChecked = data.sold
        this.imageUrl = data.image
    }

    private fun loadAndDisplayImageUri(imageUri: Uri){
        Glide.with(this)
            .load(imageUri)
            .into(binding.AddedPhoto)

        this.imageUrl = selectedImageUri.lastPathSegment.toString()
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            showProgressUI()
            //이미지를 선택한 경우
            selectedImageUri = it
            //뷰에 이미지 추가
            imageChanged = true
            loadAndDisplayImageUri(it)
            hideProgressUI()
        }
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
