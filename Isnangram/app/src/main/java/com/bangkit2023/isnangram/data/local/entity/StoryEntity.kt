package com.bangkit2023.isnangram.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
data class StoryEntity(
    @ColumnInfo(name = "photo_url")
    val photoUrl: String,

    @ColumnInfo(name = "date")
    val createdAt: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @field:SerializedName("lon")
    val lon: Double,

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @field:SerializedName("lat")
    val lat: Double

): Parcelable