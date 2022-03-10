package com.krivochkov.homework_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.krivochkov.homework_2.custom_views.EmojiProvider
import com.krivochkov.homework_2.custom_views.MessageLayout

class MainActivity : AppCompatActivity() {

    lateinit var emojiProvider: EmojiProvider
    lateinit var messageLayout: MessageLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        messageLayout = findViewById(R.id.message1)
        emojiProvider = EmojiProvider()
        messageLayout.setOnPlusClickListener {
            val emoji = emojiProvider.getRandom()
            val reactionsCount = (1..20).random()
            val isSelected = reactionsCount < 10
            messageLayout.addEmoji(emoji, reactionsCount, isSelected)
        }
        messageLayout.setOnEmojiClickListener { emojiView, isSelected ->
            when (isSelected) {
                true -> emojiView.reactionsCount++
                false -> emojiView.reactionsCount--
            }
            if (emojiView.reactionsCount < 1) {
                messageLayout.removeEmoji(emojiView)
            }
        }
        messageLayout.setUserName("Егор Кривочков")
        messageLayout.setMessage("Это моё первое сообщение")
    }
}