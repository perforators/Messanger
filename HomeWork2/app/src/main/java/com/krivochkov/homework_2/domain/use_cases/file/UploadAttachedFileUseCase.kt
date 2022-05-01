package com.krivochkov.homework_2.domain.use_cases.file

import com.krivochkov.homework_2.domain.models.AttachedFile
import io.reactivex.Single

interface UploadAttachedFileUseCase {

    operator fun invoke(attachedFile: AttachedFile): Single<String>
}