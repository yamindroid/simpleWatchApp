package com.ymo.data.model.api

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class FeedResponse(
    @field:SerializedName("msg")
    val msg: String? = null,

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: List<Feed>? = null
)

@Parcelize
data class Feed(
    @field:SerializedName("createddate")
    val createdDate: String? = null,

    @field:SerializedName("imagefile")
    val imageFile: List<String>? = null,

    @field:SerializedName("topcomments")
    val topComments: List<TopComment>? = null,

    @field:SerializedName("avatar")
    val avatar: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,
) : Parcelable

@Parcelize
@Entity(tableName = "top_comment")
data class TopComment(

    @field:SerializedName("avatar")
    val avatar: String? = null,

    @field:SerializedName("comment")
    val comment: String? = null,

    @PrimaryKey
    @field:SerializedName("id")
    val id: Int? = null,
) : Parcelable