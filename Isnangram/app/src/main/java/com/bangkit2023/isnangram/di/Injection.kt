package com.bangkit2023.isnangram.di

import android.content.Context
import com.bangkit2023.isnangram.data.StoryRepository
import com.bangkit2023.isnangram.data.local.room.datastore.UserPreferences
import com.bangkit2023.isnangram.data.local.room.datastore.UserPreferences.Companion.dataStore
import com.bangkit2023.isnangram.data.local.room.StoryDatabase
import com.bangkit2023.isnangram.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.provideApiService()
        val database = StoryDatabase.getInstance(context)
        val pref = UserPreferences(context.dataStore)
        return StoryRepository(pref, database, apiService)
    }
}