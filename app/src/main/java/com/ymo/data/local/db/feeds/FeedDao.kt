package com.ymo.data.local.db.genres

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ymo.data.model.db.FeedEntity

@Dao
interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFeeds(items: List<FeedEntity>)

    @Query("SELECT * FROM feed ORDER BY createddate DESC")
    suspend fun getFeeds(): List<FeedEntity>
}