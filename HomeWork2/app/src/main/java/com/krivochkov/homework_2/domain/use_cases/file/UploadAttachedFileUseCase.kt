package com.krivochkov.homework_2.domain.use_cases.file

import com.krivochkov.homework_2.data.repositories.AttachedFileRepositoryImpl
import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.repositories.AttachedFileRepository
import io.reactivex.Single

class UploadAttachedFileUseCase(
    private val repositoryAttached: AttachedFileRepository = AttachedFileRepositoryImpl()
) {

    operator fun invoke(attachedFile: AttachedFile): Single<String> =
        repositoryAttached.uploadFile(attachedFile)
}