package com.krivochkov.homework_2.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.krivochkov.homework_2.R

class MessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewGroup(context, attrs) {

    private val avatar: ImageView
    private val userName: TextView
    private val message: TextView
    private val emojiBox: EmojiBoxLayout

    private val radiusRounding = RADIUS_OF_ROUNDING_IN_DP.dpToPx(context)
    private val backgroundBounds = RectF()
    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor(BACKGROUND_COLOR)
    }

    init {
        inflate(context, R.layout.message_layout, this)
        avatar = findViewById(R.id.avatar)
        userName = findViewById(R.id.username)
        message = findViewById(R.id.message)
        emojiBox = findViewById(R.id.emoji_box)
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

    fun setEmojiProducer(producer: () -> Triple<String, Int, Boolean>) {
        emojiBox.emojiProducer = producer
    }

    fun addEmoji(emoji: String, reactionsCount: Int, isSelected: Boolean = false) {
        emojiBox.addEmoji(emoji, reactionsCount, isSelected)
    }

    fun removeAllEmoji() {
        emojiBox.removeAllEmoji()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (avatar.visibility != View.GONE) {
            measureChildWithMargins(
                avatar, widthMeasureSpec, 0, heightMeasureSpec, 0
            )
        }

        var heightUsed = 0
        var widthUsed = 0

        for (i in 1 until childCount) {
            val child = getChildAt(i)

            if (child.visibility != View.GONE) {
                measureChildWithMargins(
                    child,
                    widthMeasureSpec,
                    avatar.measuredWidthWithMarginsOrZero,
                    heightMeasureSpec,
                    heightUsed
                )

                widthUsed = maxOf(widthUsed, child.measuredWidthWithMarginsOrZero)
                heightUsed += child.measuredHeightWithMarginsOrZero
            }
        }

        val width = avatar.measuredWidthWithMarginsOrZero + widthUsed
        val height = maxOf(heightUsed, avatar.measuredHeightWithMarginsOrZero)

        setMeasuredDimension(
            resolveSize(width, widthMeasureSpec),
            resolveSize(height, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (avatar.visibility != View.GONE) {
            avatar.layout(
                avatar.marginLeft,
                avatar.marginTop,
                avatar.marginLeft + avatar.measuredWidth,
                avatar.paddingTop + avatar.measuredHeight
            )
        }

        var currentTop = 0

        for (i in 1 until childCount) {
            val child = getChildAt(i)

            if (child.visibility != View.GONE) {
                child.layout(
                    avatar.measuredWidthWithMarginsOrZero + child.marginLeft,
                    currentTop + child.marginTop,
                    avatar.measuredWidthWithMarginsOrZero + child.marginLeft + child.measuredWidth,
                    currentTop + child.marginTop + child.measuredHeight
                )

                currentTop += child.measuredHeightWithMarginsOrZero
            }
        }

        backgroundBounds.left = avatar.measuredWidthWithMarginsOrZero.toFloat()
        backgroundBounds.top = 0f
        backgroundBounds.right = maxOf(
                avatar.measuredWidthWithMarginsOrZero + userName.measuredWidthWithMarginsOrZero,
                avatar.measuredWidthWithMarginsOrZero + message.measuredWidthWithMarginsOrZero
        ).toFloat()
        backgroundBounds.bottom = userName.measuredHeightWithMarginsOrZero +
                    message.measuredHeightWithMarginsOrZero.toFloat()
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.drawRoundRect(backgroundBounds, radiusRounding, radiusRounding, backgroundPaint)
        super.dispatchDraw(canvas)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    private val View.measuredWidthWithMarginsOrZero: Int
        get() {
            return if (visibility != View.GONE) {
                val params = layoutParams as MarginLayoutParams
                measuredWidth + params.rightMargin + params.leftMargin
            } else {
                0
            }
        }

    private val View.measuredHeightWithMarginsOrZero: Int
        get() {
            return if (visibility != View.GONE) {
                val params = layoutParams as MarginLayoutParams
                measuredHeight + params.topMargin + params.bottomMargin
            } else {
                0
            }
        }

    companion object {
        private const val BACKGROUND_COLOR = "#1c1c1c"
        private const val RADIUS_OF_ROUNDING_IN_DP = 8
    }
}