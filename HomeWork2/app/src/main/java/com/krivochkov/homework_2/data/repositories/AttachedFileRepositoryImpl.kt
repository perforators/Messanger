package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.data.sources.remote.data_sources.FileRemoteDataSource
import com.krivochkov.homework_2.data.sources.remote.data_sources.FileRemoteDataSourceImpl
import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.repositories.AttachedFileRepository
import io.reactivex.Single

class AttachedFileRepositoryImpl(
    private val fileRemoteDataSource: FileRemoteDataSource = FileRemoteDataSourceImpl()
) : AttachedFileRepository {

    override fun uploadFile(attachedFile: AttachedFile): Single<Pair<String, String>> {
        return fileRemoteDataSource.uploadFile(attachedFile.localPath, attachedFile.type)
    }
}