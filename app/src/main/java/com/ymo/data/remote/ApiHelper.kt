package com.ymo.data.remote

import com.ymo.data.model.api.FeedResponse

interface ApiHelper {
    suspend fun loadNewFeeds(pageNum: Int): FeedResponse
}
