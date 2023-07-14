package com.bangkit2023.isnangram.main.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit2023.isnangram.data.StoryRepository
import com.bangkit2023.isnangram.data.remote.response.LoginResponse
import com.bangkit2023.isnangram.data.remote.response.RegisterResponse
import com.bangkit2023.isnangram.utils.Result

class AuthViewModel(private val repository: StoryRepository): ViewModel() {
    fun userLogin(email: String, password: String): LiveData<Result<LoginResponse>> =
        repository.userLogin(email, password)

    fun userRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> =
        repository.userRegister(name, email, password)
}