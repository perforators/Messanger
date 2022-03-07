package com.krivochkov.homework_2.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.krivochkov.homework_2.R

class MessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewGroup(context, attrs) {

    var onEmojiClick: (EmojiView) -> Unit = { emoji ->
        emoji.apply {
            when (isSelected) {
                true -> reactionsCount++
                false -> reactionsCount--
            }
            if (reactionsCount < 1) {
                flexBox.removeView(this)
            }
        }
    }
    var onPlusClick: (ImageView) -> Unit = {
        addEmoji(
            EmojiView.DEFAULT_EMOJI,
            EmojiView.DEFAULT_REACTION_COUNT,
            false
        )
    }

    private val avatar: ImageView
    private val userName: TextView
    private val message: TextView
    private val flexBox: FlexBoxLayout

    private val defaultMargin = resources.getDimension(R.dimen.small_margin).toInt()
    private val radiusRounding = resources.getDimension(R.dimen.radius_rounding)
    private val backgroundBounds = RectF()
    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.black_200)
    }

    private val plus = ImageView(context).apply {
        layoutParams = createDefaultLayoutParams()
        background = AppCompatResources.getDrawable(context, R.drawable.bg_emoji_view_unselected)
        setImageResource(R.drawable.plus)
        setPadding(resources.getDimension(R.dimen.small_padding).toInt())
    }

    init {
        inflate(context, R.layout.message_layout, this)
        avatar = findViewById(R.id.avatar)
        userName = findViewById(R.id.username)
        message = findViewById(R.id.message)
        flexBox = findViewById(R.id.flex_box)

        plus.setOnClickListener {
            onPlusClick(plus)
        }
        flexBox.addView(plus)
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

    fun addEmoji(emoji: String, reactionsCount: Int, isSelected: Boolean = false): Boolean {
        if (reactionsCount < 1) {
            return false
        }

        val emojiView = EmojiView(context).apply {
            layoutParams = createDefaultLayoutParams()
            background = AppCompatResources.getDrawable(context, R.drawable.bg_emoji_view)
            this.emoji = emoji
            this.reactionsCount = reactionsCount
            this.isSelected = isSelected
        }
        emojiView.onClick = {
            onEmojiClick(it)
        }
        flexBox.addView(emojiView, flexBox.childCount - 1)

        return true
    }

    fun removeAllEmoji() {
        flexBox.removeAllViews()
        flexBox.addView(plus)
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

        val width = avatar.measuredWidthWithMarginsOrZero + widthUsed + paddingLeft + paddingRight
        val height = maxOf(heightUsed, avatar.measuredHeightWithMarginsOrZero) +
                paddingTop + paddingBottom

        setMeasuredDimension(
            resolveSize(width, widthMeasureSpec),
            resolveSize(height, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (avatar.visibility != View.GONE) {
            avatar.layout(
                avatar.marginLeft + paddingLeft,
                avatar.marginTop + paddingTop,
                avatar.marginLeft + paddingLeft + avatar.measuredWidth,
                avatar.paddingTop + paddingTop + avatar.measuredHeight
            )
        }

        var currentTop = paddingTop
        val currentLeft = avatar.measuredWidthWithMarginsOrZero + paddingLeft

        for (i in 1 until childCount) {
            val child = getChildAt(i)

            if (child.visibility != View.GONE) {
                child.layout(
                    currentLeft + child.marginLeft,
                    currentTop + child.marginTop,
                    currentLeft + child.marginLeft + child.measuredWidth,
                    currentTop + child.marginTop + child.measuredHeight
                )

                currentTop += child.measuredHeightWithMarginsOrZero
            }
        }

        backgroundBounds.left = currentLeft.toFloat()
        backgroundBounds.top = paddingTop.toFloat()
        backgroundBounds.right = maxOf(
                currentLeft + userName.measuredWidthWithMarginsOrZero,
                currentLeft + message.measuredWidthWithMarginsOrZero
        ).toFloat()
        backgroundBounds.bottom = paddingTop + userName.measuredHeightWithMarginsOrZero +
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

    private fun createDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(defaultMargin)
        }
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
}