package com.bangkit2023.isnangram.main.ui.upload

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit2023.isnangram.R
import com.bangkit2023.isnangram.databinding.ActivityUploadBinding
import com.bangkit2023.isnangram.main.viewmodel.ViewModelFactory
import com.bangkit2023.isnangram.utils.Helper
import com.bangkit2023.isnangram.utils.Result
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@Suppress("DEPRECATION")
class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var viewModel: UploadViewModel
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[UploadViewModel::class.java]

        setSupportActionBar(binding.toolbarUpload)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val myFile = intent?.getSerializableExtra(EXTRA_PHOTO_RESULT) as File
        val isBackCamera = intent?.getBooleanExtra(EXTRA_CAMERA_MODE, true) as Boolean
        val rotatedBitmap = Helper.rotateBitmap(
            BitmapFactory.decodeFile(myFile.path),
            isBackCamera
        )

        binding.toolbarUpload.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.ivPreview.setImageBitmap(rotatedBitmap)

        val uploadImage = Helper.reduceFileSize(myFile)
        val requestImage = uploadImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val reqImageFile = MultipartBody.Part.createFormData("photo", myFile.name, requestImage)

        viewModel.getUserToken().observe(this) {
            if (it != null) {
                token = it
            }
        }

        binding.buttonAdd.setOnClickListener {
            binding.progressCircular.visibility = View.VISIBLE
            uploadStory(token, reqImageFile)
        }
    }


    private fun uploadStory(token: String, image: MultipartBody.Part) {
        val desc = binding.edAddDescription.text.toString()
        val description = desc.trim()

        if (description.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_caption_empty), Toast.LENGTH_LONG).show()
        } else {
            val descriptionRequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

            viewModel.uploadStory(token, image, descriptionRequestBody)
                .observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Success -> {
                                binding.progressCircular.visibility = View.GONE
                                Toast.makeText(this, "Story uploaded", Toast.LENGTH_LONG).show()
                                setResult(RESULT_OK)
                                finish()
                            }
                            is Result.Error -> {
                                binding.progressCircular.visibility = View.GONE
                                Toast.makeText(
                                    this,
                                    "Something went wrong, try again",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            is Result.Loading -> {
                            }
                        }
                    }
                }
        }
    }

    companion object {
        const val EXTRA_PHOTO_RESULT = "PHOTO_RESULT"
        const val EXTRA_CAMERA_MODE = "CAMERA_MODE"
    }
}
