package com.ymo.data.remote.service

import com.ymo.data.model.api.FeedResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("article/feed")
    suspend fun loadNewFeeds(
        @Query("usertoken") userToken: String,
        @Query("pagenum") pageNum: Int,
        @Query("pagesize") pageSize: Int,
    ): FeedResponse
}