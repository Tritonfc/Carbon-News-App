package com.tobe.carbonnewsapp.data.localDataSource

import androidx.room.TypeConverter
import com.tobe.carbonnewsapp.data.models.Source


class Converters {


    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(string: String): Source {
        return Source(string, string)
    }
}