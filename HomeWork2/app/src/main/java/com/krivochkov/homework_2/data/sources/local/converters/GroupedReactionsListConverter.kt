package com.krivochkov.homework_2.data.sources.local.converters

import androidx.room.TypeConverter
import com.krivochkov.homework_2.domain.models.GroupedReaction
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GroupedReactionsListConverter {

    @TypeConverter
    fun convertGroupedReactionsListToString(value : List<GroupedReaction>?) =
        Json.encodeToString(value)

    @TypeConverter
    fun convertStringToGroupedReactionsList(value: String) =
        Json.decodeFromString<List<GroupedReaction>>(value)
}