package com.krivochkov.homework_2.data.sources.remote.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileResponse(
    @SerialName("uri") val uri: String
)