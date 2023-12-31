    package com.bangkit2023.isnangram.data.remote.response

    sealed class ApiResponse<out R> {
        data class Success<out T>(val data: T) : ApiResponse<T>()
        data class Error(val errorMessage: String) : ApiResponse<Nothing>()
    }