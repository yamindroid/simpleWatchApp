package com.ymo.data

import com.ymo.data.local.db.DBHelper
import com.ymo.data.mapper.toEntityList
import com.ymo.data.mapper.toModelList
import com.ymo.data.model.api.Feed
import com.ymo.data.model.api.FeedResponse
import com.ymo.data.model.db.FeedEntity
import com.ymo.data.remote.ApiHelper
import com.ymo.di.NetworkModule
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val network: NetworkModule.Network,
    private val apiHelper: ApiHelper,
    private val dbHelper: DBHelper
) : DataRepositoryHelper {

    override suspend fun getAllFeeds(page: Int) : List<Feed>? =
        if (network.isConnected){
            loadFeedsFromAPI(page)
        }else {
            getFeedsFromDB().toModelList()
        }

    override suspend fun loadFeedsFromAPI(page: Int): List<Feed>? {
        val response = apiHelper.loadNewFeeds(page)
        // Refresh feeds in database
        response.data.toEntityList()?.let { feeds ->
            dbHelper.insertAllFeeds(feeds)
        }
        return response.data
    }

    override suspend fun getFeedsFromDB(): List<FeedEntity> = dbHelper.getFeeds()

    override suspend fun loadNewFeeds(pageNum: Int): FeedResponse =
      apiHelper.loadNewFeeds(pageNum)

    override suspend fun getFeeds(): List<FeedEntity> =
       dbHelper.getFeeds()

    override suspend fun insertAllFeeds(feeds: List<FeedEntity>) =
      dbHelper.insertAllFeeds(feeds)
}
