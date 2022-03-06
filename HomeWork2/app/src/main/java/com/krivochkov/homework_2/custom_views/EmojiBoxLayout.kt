package com.krivochkov.homework_2.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.*
import com.krivochkov.homework_2.R

class EmojiBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewGroup(context, attrs) {

    var emojiProducer: () -> Pair<String, Int> = {
        EmojiView.DEFAULT_EMOJI to EmojiView.DEFAULT_REACTION_COUNT
    }

    private val defaultMargin = DEFAULT_MARGIN_IN_DP.dpToPx(context).toInt()

    private val plus = ImageView(context).apply {
        layoutParams = createDefaultLayoutParams()
        background = AppCompatResources.getDrawable(context, R.drawable.bg_emoji_view_unselected)
        setImageResource(R.drawable.plus)
        setPadding(PLUS_PADDING_IN_DP.dpToPx(context).toInt())
    }

    init {
        addView(plus)
        plus.setOnClickListener {
            val (emoji, reactionsCount) = emojiProducer()
            addEmoji(emoji, reactionsCount)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        var widthCurrentLine = 0
        var maxHeightCurrentLine = 0
        var maxWidthLines = 0
        var totalHeight = 0

        children.forEach { child ->
            if (child.visibility != View.GONE) {
                measureChildWithMargins(
                    child, widthMeasureSpec, 0, heightMeasureSpec, totalHeight
                )
                if (widthCurrentLine + child.measuredWidthWithMargins > maxWidth) {
                    maxWidthLines = maxOf(maxWidthLines, widthCurrentLine)
                    widthCurrentLine = child.measuredWidthWithMargins
                    totalHeight += maxHeightCurrentLine
                    maxHeightCurrentLine = child.measuredHeightWithMargins
                } else {
                    maxHeightCurrentLine =
                        maxOf(maxHeightCurrentLine, child.measuredHeightWithMargins)
                    widthCurrentLine += child.measuredWidthWithMargins
                }
            }
        }

        maxWidthLines = maxOf(maxWidthLines, widthCurrentLine)
        totalHeight += maxHeightCurrentLine

        setMeasuredDimension(
            resolveSize(maxWidthLines, widthMeasureSpec),
            resolveSize(totalHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeftPosition = 0
        var currentTopPosition = 0
        var heightCurrentLine = 0

        children.forEach { child ->
            if (child.visibility != View.GONE) {
                if (currentLeftPosition + child.measuredWidthWithMargins > measuredWidth) {
                    currentLeftPosition = 0
                    currentTopPosition += heightCurrentLine
                    heightCurrentLine = child.measuredHeightWithMargins
                } else {
                    heightCurrentLine = maxOf(child.measuredHeightWithMargins, heightCurrentLine)
                }
                child.layout(
                    currentLeftPosition + child.marginLeft,
                    currentTopPosition + child.marginTop,
                    currentLeftPosition + child.marginLeft + child.measuredWidth,
                    currentTopPosition + child.marginTop + child.measuredHeight
                )
                currentLeftPosition += child.measuredWidthWithMargins
            }
        }
    }

    fun addEmoji(emoji: String, reactionsCount: Int) {
        val emojiView = EmojiView(context).apply {
            layoutParams = createDefaultLayoutParams()
            background = AppCompatResources.getDrawable(context, R.drawable.bg_emoji_view)
            this.emoji = emoji
            this.reactionsCount = reactionsCount
        }
        removeView(plus)
        addView(emojiView)
        addView(plus)
    }

    fun removeAllEmoji() {
        removeAllViews()
        addView(plus)
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

    private val View.measuredWidthWithMargins: Int
        get() {
            val params = layoutParams as MarginLayoutParams
            return measuredWidth + params.rightMargin + params.leftMargin
        }

    private val View.measuredHeightWithMargins: Int
        get() {
            val params = layoutParams as MarginLayoutParams
            return measuredHeight + params.topMargin + params.bottomMargin
        }

    companion object {
        private const val PLUS_PADDING_IN_DP = 6
        private const val DEFAULT_MARGIN_IN_DP = 4
    }
}
