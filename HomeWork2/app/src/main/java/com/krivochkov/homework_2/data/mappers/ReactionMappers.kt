package com.krivochkov.homework_2.data.mappers

import com.krivochkov.homework_2.data.sources.remote.dto.ReactionDto
import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.domain.models.Reaction
import com.krivochkov.homework_2.utils.convertEmojiToUtf

fun ReactionDto.mapToReaction() = Reaction(
    userId = userId,
    emoji = Emoji(emojiName, emojiCode.convertEmojiToUtf())
)