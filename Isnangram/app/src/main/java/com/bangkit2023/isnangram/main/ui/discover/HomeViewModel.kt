package com.bangkit2023.isnangram.main.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit2023.isnangram.data.StoryRepository
import com.bangkit2023.isnangram.data.local.entity.StoryEntity

@ExperimentalPagingApi
class HomeViewModel(private val repository: StoryRepository): ViewModel() {
    fun getStories(token: String): LiveData<PagingData<StoryEntity>> =
        repository.getStories(token).cachedIn(viewModelScope)

    suspend fun userLogout() = repository.clearLoginSession()
}