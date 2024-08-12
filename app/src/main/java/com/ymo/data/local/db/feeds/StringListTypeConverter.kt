package com.ymo.data.local.db.feeds

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListTypeConverter {

    @TypeConverter
    fun saveStringList(list: List<String?>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun getStringList(list: String?): List<String?>? {
        return Gson().fromJson(
            list.toString(),
            object : TypeToken<List<String?>?>() {}.type
        )
    }
}