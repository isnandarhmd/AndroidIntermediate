package com.bangkit2023.isnangram.main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.bangkit2023.isnangram.data.StoryRepository
import com.bangkit2023.isnangram.di.Injection
import com.bangkit2023.isnangram.main.ui.auth.AuthViewModel
import com.bangkit2023.isnangram.main.ui.discover.HomeViewModel
import com.bangkit2023.isnangram.main.ui.explore.StoryWithMapsViewModel
import com.bangkit2023.isnangram.main.ui.splash.SplashViewModel
import com.bangkit2023.isnangram.main.ui.upload.UploadViewModel

class ViewModelFactory private constructor(
    private val repository: StoryRepository
): ViewModelProvider.NewInstanceFactory(){

    @OptIn(ExperimentalPagingApi::class)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StoryWithMapsViewModel::class.java) -> {
                return StoryWithMapsViewModel(repository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}