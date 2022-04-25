package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.data.sources.remote.data_sources.FileRemoteDataSource
import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.repositories.AttachedFileRepository

class AttachedFileRepositoryImpl(
    private val fileRemoteDataSource: FileRemoteDataSource
) : AttachedFileRepository {

    override fun uploadFile(attachedFile: AttachedFile) =
        fileRemoteDataSource.uploadFile(attachedFile.localPath, attachedFile.type)
}