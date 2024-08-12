package com.ymo.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ymo.data.local.db.feeds.StringListTypeConverter
import com.ymo.data.local.db.feeds.TopCommentListTypeConverter
import com.ymo.data.model.api.TopComment

@Entity(tableName = "feed")
data class FeedEntity @JvmOverloads constructor(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,

    @ColumnInfo(name = "avatar")
    val avatar: String? = null,

    @ColumnInfo(name = "createddate")
    val createdDate: String? = null,

    @TypeConverters(TopCommentListTypeConverter::class)
    val topComments: List<TopComment>? = null,

    @TypeConverters(StringListTypeConverter::class)
    val imagefile: List<String>? = null
)