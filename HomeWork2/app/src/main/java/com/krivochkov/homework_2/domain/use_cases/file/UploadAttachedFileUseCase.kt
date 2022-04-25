package com.krivochkov.homework_2.domain.use_cases.file

import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.repositories.AttachedFileRepository

class UploadAttachedFileUseCase(private val repositoryAttached: AttachedFileRepository) {

    operator fun invoke(attachedFile: AttachedFile) = repositoryAttached.uploadFile(attachedFile)
}