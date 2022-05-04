package com.krivochkov.homework_2.domain.use_cases.file

import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.repositories.AttachedFileRepository
import javax.inject.Inject

class UploadAttachedFileUseCaseImpl @Inject constructor(
    private val repository: AttachedFileRepository
) : UploadAttachedFileUseCase {

    override operator fun invoke(attachedFile: AttachedFile) = repository.uploadFile(attachedFile)
}
