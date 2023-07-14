package com.bangkit2023.isnangram.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.bangkit2023.isnangram.data.remote.response.StoriesResponse

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

fun <T> LiveData<Result<T>>.mapResultToResponse(): LiveData<Result<StoriesResponse>> =
    map { result ->
        when (result) {
            is Result.Success -> Result.Success(result.data as StoriesResponse)
            is Result.Error -> Result.Error(result.error)
            is Result.Loading -> Result.Loading
        }
    }