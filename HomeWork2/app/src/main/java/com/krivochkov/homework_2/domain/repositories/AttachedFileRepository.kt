package com.krivochkov.homework_2.domain.repositories

import com.krivochkov.homework_2.domain.models.AttachedFile
import io.reactivex.Single

interface AttachedFileRepository {

    fun uploadFile(attachedFile: AttachedFile): Single<Pair<String, String>>
}