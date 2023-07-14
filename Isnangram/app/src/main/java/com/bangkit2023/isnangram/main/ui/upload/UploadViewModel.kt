package com.bangkit2023.isnangram.main.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit2023.isnangram.data.StoryRepository
import com.bangkit2023.isnangram.data.remote.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.bangkit2023.isnangram.utils.Result

class UploadViewModel(private val repository: StoryRepository): ViewModel() {
    fun getUserToken(): LiveData<String?> {
        return repository.getLoginSession()
    }
    fun uploadStory(
        token: String,
        image: MultipartBody.Part,
        description: RequestBody,
    ): LiveData<Result<UploadResponse>> {
        return repository.uploadStory(token, description, image)
    }
}