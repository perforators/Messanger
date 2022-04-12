package com.krivochkov.homework_2.data.sources.remote.data_sources

import io.reactivex.Single

interface FileRemoteDataSource {

    fun uploadFile(path: String, type: String): Single<String>
}