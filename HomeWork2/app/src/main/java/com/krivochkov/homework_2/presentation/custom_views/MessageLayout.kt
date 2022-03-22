package com.krivochkov.homework_2.presentation.custom_views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.domain.models.GroupedReaction

class MessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private var onEmojiClick: (EmojiView, Boolean) -> Unit = { _, _ -> }
    private var onPlusClick: (ImageView) -> Unit = {  }

    private val avatar: ImageView
    private val userName: TextView
    private val message: TextView
    private val messageBox: LinearLayout
    private val messageBoxContent: LinearLayout
    private val flexBox: FlexBoxLayout

    private val defaultMargin = resources.getDimension(R.dimen.small_margin).toInt()

    private val plus = ImageView(context).apply {
        layoutParams = createDefaultLayoutParams()
        background = getDrawable(context, R.drawable.bg_emoji_view_unselected)
        setImageResource(R.drawable.plus)
        setPadding(resources.getDimension(R.dimen.small_padding).toInt())
    }

    var isMyMessage: Boolean = false
        set(value) {
            flexBox.isReversed = value
            if (value) {
                messageBoxContent.background = getDrawable(context, R.drawable.bg_message_is_me)
                messageBox.gravity = Gravity.END
                avatar.visibility = View.GONE
                userName.visibility = View.GONE
            } else {
                messageBoxContent.background = getDrawable(context, R.drawable.bg_message_not_me)
                messageBox.gravity = Gravity.START
                avatar.visibility = View.VISIBLE
                userName.visibility = View.VISIBLE
            }
            field = value
        }

    init {
        inflate(context, R.layout.message_layout, this)
        avatar = findViewById(R.id.avatar)
        userName = findViewById(R.id.username)
        message = findViewById(R.id.message)
        messageBox = findViewById(R.id.message_box)
        messageBoxContent = findViewById(R.id.message_content)
        flexBox = findViewById(R.id.flex_box)

        plus.setOnClickListener {
            onPlusClick(plus)
        }
        flexBox.addView(plus)
        hidePlus()
    }

    fun showPlus() {
        plus.visibility = View.VISIBLE
    }

    fun hidePlus() {
        plus.visibility = View.GONE
    }

    fun setOnEmojiClickListener(onClick: (EmojiView, Boolean) -> Unit) {
        onEmojiClick = onClick
    }

    fun setOnPlusClickListener(onClick: (ImageView) -> Unit) {
        onPlusClick = onClick
    }

    fun setAvatar(@DrawableRes resId: Int) {
        avatar.setImageResource(resId)
    }

    fun setAvatar(drawable: Drawable) {
        avatar.setImageDrawable(drawable)
    }

    fun setUserName(newUserName: String) {
        userName.text = newUserName
    }

    fun setMessage(newMessage: String) {
        message.text = newMessage
    }

    fun addReactions(reactions: List<GroupedReaction>) {
        for (reaction in reactions) {
            val (emoji, reactionsCount, isSelected) = reaction
            addReaction(emoji, reactionsCount, isSelected)
        }
    }

    fun addReaction(emoji: String, reactionsCount: Int, isSelected: Boolean = false): Boolean {
        if (reactionsCount < 1) {
            return false
        }

        val emojiView = EmojiView(context).apply {
            layoutParams = createDefaultLayoutParams()
            background = getDrawable(context, R.drawable.bg_emoji_view)
            this.emoji = emoji
            this.reactionsCount = reactionsCount
            this.isSelected = isSelected
        }
        emojiView.setOnChangedSelectListener { selectedEmoji, newSelect ->
            onEmojiClick(selectedEmoji, newSelect)
        }
        flexBox.addView(emojiView, flexBox.childCount - 1)

        return true
    }

    fun removeEmoji(emoji: EmojiView) {
        flexBox.removeView(emoji)
    }

    fun removeAllEmoji() {
        flexBox.removeAllViews()
        flexBox.addView(plus)
    }

    private fun createDefaultLayoutParams(): MarginLayoutParams {
        return MarginLayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(defaultMargin)
        }
    }
}