package com.ymo.data.local.db

import com.ymo.data.model.db.FeedEntity

interface DBHelper {
    suspend fun getFeeds(): List<FeedEntity>
    suspend fun insertAllFeeds(feeds : List<FeedEntity>)
    suspend fun isDatabaseEmpty(): Boolean
}