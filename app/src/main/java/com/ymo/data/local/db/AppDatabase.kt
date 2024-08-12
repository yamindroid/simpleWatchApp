package com.ymo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ymo.data.local.db.feeds.StringListTypeConverter
import com.ymo.data.local.db.feeds.TopCommentListTypeConverter
import com.ymo.data.local.db.genres.FeedDao
import com.ymo.data.model.api.TopComment
import com.ymo.data.model.db.FeedEntity

@Database(entities = [FeedEntity::class, TopComment::class], version = 1)
@TypeConverters(TopCommentListTypeConverter::class, StringListTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao
}