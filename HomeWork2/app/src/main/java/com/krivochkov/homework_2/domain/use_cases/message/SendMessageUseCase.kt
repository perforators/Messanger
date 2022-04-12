package com.krivochkov.homework_2.domain.use_cases.message

import com.krivochkov.homework_2.data.repositories.MessageRepositoryImpl
import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.repositories.MessageRepository
import com.krivochkov.homework_2.domain.use_cases.file.UploadAttachedFileUseCase
import io.reactivex.Completable
import io.reactivex.Observable

class SendMessageUseCase(
    private val repository: MessageRepository = MessageRepositoryImpl(),
    private val uploadAttachedFileUseCase: UploadAttachedFileUseCase = UploadAttachedFileUseCase()
) {

    operator fun invoke(
        channelName: String,
        topicName: String,
        message: String,
        attachedFiles: List<AttachedFile>
    ): Completable {
        return Observable.fromIterable(attachedFiles)
            .flatMap { file ->
                uploadAttachedFileUseCase(file)
                    .filter { it.remotePath != null }
                    .map { attachedFile -> "[${attachedFile.name}](${attachedFile.remotePath})" }
                    .toObservable()
            }
            .toList()
            .flatMapCompletable {
                val resultMessage = if (it.isEmpty()) message else "$message\n$it"
                repository.sendMessage(channelName, topicName, resultMessage)
            }
    }
}