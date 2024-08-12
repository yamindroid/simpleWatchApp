package com.ymo.data.remote

import android.util.Log
import com.ymo.BuildConfig
import com.ymo.data.model.api.FeedResponse
import com.ymo.data.remote.service.ApiService
import javax.inject.Inject

private const val PAGE_SIZE = 5

class ApiHelperImpl @Inject
constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun loadNewFeeds(pageNum: Int): FeedResponse {
        Log.e("////", "loadNewFeeds: calling api arrived... ")
        return apiService.loadNewFeeds(BuildConfig.USER_TOKEN, pageNum, PAGE_SIZE)
    }
}
