package com.ymo.data

import com.ymo.data.local.db.DBHelper
import com.ymo.data.model.api.Feed
import com.ymo.data.model.db.FeedEntity
import com.ymo.data.remote.ApiHelper

interface DataRepositoryHelper : ApiHelper, DBHelper {
    suspend fun getAllFeeds(page: Int): List<Feed>?
    suspend fun loadFeedsFromAPI(page: Int): List<Feed>?
    suspend fun getFeedsFromDB(): List<FeedEntity>
    suspend fun getLastPageNumber(): Int
    suspend fun isTryingToGetExistingData(page: Int): Boolean
}