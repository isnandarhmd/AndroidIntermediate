package com.bangkit2023.isnangram.main.ui.explore

import androidx.lifecycle.ViewModel
import com.bangkit2023.isnangram.data.StoryRepository
import com.bangkit2023.isnangram.data.remote.response.StoriesResponse
import com.bangkit2023.isnangram.utils.mapResultToResponse

class StoryWithMapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStoryWithMaps(location: Int, token: String) =
        storyRepository.getStoryWithMaps(location, token)
}