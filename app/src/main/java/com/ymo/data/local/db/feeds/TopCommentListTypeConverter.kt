package com.ymo.data.local.db.feeds

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ymo.data.model.api.TopComment

class TopCommentListTypeConverter {

    @TypeConverter
    fun saveTopCommentList(list: List<TopComment?>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun getTopCommentList(list: String?): List<TopComment?>? {
        return Gson().fromJson(
            list.toString(),
            object : TypeToken<List<TopComment?>?>() {}.type
        )
    }
}