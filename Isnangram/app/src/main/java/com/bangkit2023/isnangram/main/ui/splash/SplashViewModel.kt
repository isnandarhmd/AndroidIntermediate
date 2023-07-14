package com.bangkit2023.isnangram.main.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit2023.isnangram.data.StoryRepository

class SplashViewModel(private val repository: StoryRepository): ViewModel() {
    fun getUserToken(): LiveData<String?> {
        return repository.getLoginSession()
    }
}