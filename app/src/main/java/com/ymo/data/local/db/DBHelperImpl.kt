package com.ymo.data.local.db

import com.ymo.data.model.db.FeedEntity
import javax.inject.Inject

private const val PAGE_SIZE = 5

class DBHelperImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : DBHelper {
    override suspend fun getFeeds(): List<FeedEntity> =
        appDatabase.feedDao().getFeeds()

    override suspend fun insertAllFeeds(feeds: List<FeedEntity>) =
        appDatabase.feedDao().insertAllFeeds(feeds)
}