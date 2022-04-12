package com.krivochkov.homework_2.data.sources.remote.data_sources

import com.krivochkov.homework_2.data.sources.remote.api.ZulipApi
import com.krivochkov.homework_2.data.sources.remote.api.ZulipApiProvider
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class FileRemoteDataSourceImpl(
    private val api: ZulipApi = ZulipApiProvider.zulipApi
) : FileRemoteDataSource {

    override fun uploadFile(path: String, type: String): Single<String> {
        val file = File(path)
        val requestFile = file.asRequestBody(type.toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData(DEFAULT_NAME, file.name, requestFile)
        return api.uploadFile(body).map { it.uri }
    }

    companion object {

        private const val DEFAULT_NAME = "UPLOAD"
    }
}