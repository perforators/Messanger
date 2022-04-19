package com.krivochkov.homework_2.data.sources.local.converters

import androidx.room.TypeConverter
import com.krivochkov.homework_2.domain.models.Reaction
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ReactionsListConverter {

    @TypeConverter
    fun convertReactionsListToString(value : List<Reaction>?) = Json.encodeToString(value)

    @TypeConverter
    fun convertStringToReactionsList(value: String) = Json.decodeFromString<List<Reaction>>(value)
}