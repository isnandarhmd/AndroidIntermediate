package com.bangkit2023.isnangram.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.bangkit2023.isnangram.data.local.room.datastore.UserPreferences
import com.bangkit2023.isnangram.data.local.entity.StoryEntity
import com.bangkit2023.isnangram.data.local.room.StoryDatabase
import com.bangkit2023.isnangram.data.remote.StoryRemoteMediator
import com.bangkit2023.isnangram.data.remote.response.LoginResponse
import com.bangkit2023.isnangram.data.remote.response.RegisterResponse
import com.bangkit2023.isnangram.data.remote.response.StoriesResponse
import com.bangkit2023.isnangram.data.remote.response.UploadResponse
import com.bangkit2023.isnangram.data.remote.retrofit.ApiService
import com.bangkit2023.isnangram.utils.Result
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val pref: UserPreferences,
    private val database: StoryDatabase,
    private val apiService: ApiService
) {

    fun userLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        try {
            val response = apiService.userLogin(email, password)
            val token = response.loginResult.token
            pref.saveLoginSession(token)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.toString()))
        }

    }

    fun userRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        try {
            val response = apiService.userRegister(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.toString()))
        }
    }

    fun getStoryWithMaps(location: Int, token: String): LiveData<Result<StoriesResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getStoriesWithLocation(location, token)
                emit(Result.Success(response))
            } catch (e: java.lang.Exception) {
                Log.d("Signup", e.message.toString())
                emit(Result.Error(e.message.toString()))
            }
        }

    fun uploadStory(
        token: String,
        description: RequestBody,
        image: MultipartBody.Part,
    ): LiveData<Result<UploadResponse>> = liveData {
        val getToken = generateBearerToken(token)
        try {
            val response = apiService.uploadStory(
                getToken, image, description
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("UPLOAD", e.toString())
            emit(Result.Error(e.toString()))
        }
    }

    fun getLoginSession(): LiveData<String?> {
        return pref.getUserToken().asLiveData()
    }

    suspend fun clearLoginSession() {
        return pref.clearLoginSession()
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(token: String): LiveData<PagingData<StoryEntity>> {
        val getToken = generateBearerToken(token)
        return Pager(
            config = PagingConfig(
                10
            ),
            remoteMediator = StoryRemoteMediator(
                database,
                apiService,
                getToken
            ),
            pagingSourceFactory = {
                database.storyDao().getStories()
            }
        ).liveData
    }

    private fun generateBearerToken(token: String): String {
        return "Bearer $token"
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            pref: UserPreferences,
            database: StoryDatabase,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(pref, database, apiService)
            }.also { instance = it }
    }
}