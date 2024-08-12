package com.ymo.data

import android.util.Log
import com.ymo.data.local.db.DBHelper
import com.ymo.data.local.pref.PreferencesHelper
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
    private val dbHelper: DBHelper,
    private val preferenceHelper : PreferencesHelper
) : DataRepositoryHelper {

    override suspend fun getAllFeeds(page: Int) : List<Feed>? {
        Log.e("/////", "arrived getAllFeeds: isTryingToGetExistingData(page) ${isTryingToGetExistingData(page)}", )
     return   if (network.isConnected && !isTryingToGetExistingData(page)) {
         Log.e("/////", "getAllFeeds:loadFeedsFromAPI arrived " )
            loadFeedsFromAPI(page)
        }else {
         Log.e("/////", "getAllFeeds:getFeedsFromDB arrived " )
            getFeedsFromDB().toModelList()
        }
    }

    override suspend fun isTryingToGetExistingData(page: Int): Boolean {
        Log.e("/////", "isTryingToGetExistingData: arrived...", )
        return (page <= preferenceHelper.lastPageNumber) && !isDatabaseEmpty()
    }

//    private fun isTryingToGetExistingData(page: Int) = ( page < preferenceHelper.lastPageNumber) && !dbHelper.isDatabaseEmpty()

    override suspend fun loadFeedsFromAPI(page: Int): List<Feed>? {
        preferenceHelper.lastPageNumber = page
        val response = apiHelper.loadNewFeeds(page)

        isDatabaseEmpty()

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

    override suspend fun isDatabaseEmpty(): Boolean = dbHelper.isDatabaseEmpty()

    override suspend fun getLastPageNumber() = preferenceHelper.lastPageNumber
}
