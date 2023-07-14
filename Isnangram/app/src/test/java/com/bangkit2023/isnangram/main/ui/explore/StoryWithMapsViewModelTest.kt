package com.bangkit2023.isnangram.main.ui.explore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.bangkit2023.isnangram.data.StoryRepository
import com.bangkit2023.isnangram.data.remote.response.StoriesResponse
import com.bangkit2023.isnangram.utils.DataDummy
import com.bangkit2023.isnangram.utils.Result
import com.bangkit2023.isnangram.utils.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryWithMapsViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyWithMapsViewModel: StoryWithMapsViewModel
    private val dummyStoryWithMaps = DataDummy.generateDummyStoryWithMapsResponse()

    @Before
    fun setUp() {
        storyWithMapsViewModel = StoryWithMapsViewModel(storyRepository)
    }

    @Test
    fun `when Get Story With Maps Should Not Null and Return Success`() {
        val expectedStory = MutableLiveData<Result<StoriesResponse>>()
        expectedStory.value = Result.Success(dummyStoryWithMaps)
        Mockito.`when`(storyWithMapsViewModel.getStoryWithMaps(LOCATION, TOKEN)).thenReturn(expectedStory)

        val actualStory = storyWithMapsViewModel.getStoryWithMaps(LOCATION, TOKEN).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoryWithMaps(LOCATION, TOKEN)
        assertNotNull(actualStory)
        assertTrue(actualStory is Result.Success)
    }

    companion object {
        private const val LOCATION = 1
        private const val TOKEN = "Bearer TOKEN"
    }
}