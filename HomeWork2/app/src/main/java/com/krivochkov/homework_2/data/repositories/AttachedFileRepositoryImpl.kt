package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.data.sources.remote.data_sources.FileRemoteDataSource
import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.repositories.AttachedFileRepository
import javax.inject.Inject

class AttachedFileRepositoryImpl @Inject constructor(
    private val fileRemoteDataSource: FileRemoteDataSource
) : AttachedFileRepository {

    override fun uploadFile(attachedFile: AttachedFile) =
        fileRemoteDataSource.uploadFile(attachedFile.localPath, attachedFile.type)
}