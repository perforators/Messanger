package com.krivochkov.homework_2.custom_views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.krivochkov.homework_2.R

class EmojiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private val reactionText: String
        get() = "$emoji  $reactionsCount"

    var reactionsCount = DEFAULT_REACTION_COUNT
        set(value) {
            field = value
            requestLayout()
        }

    var emoji = DEFAULT_EMOJI
        set(value) {
            field = value
            requestLayout()
        }

    private val textPaint = TextPaint().apply {
        isAntiAlias = true
    }
    private val reactionsTextBounds = Rect()
    private val textPoint = PointF()

    init {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.EmojiView)

        emoji = typedArray.getString(R.styleable.EmojiView_emoji) ?: DEFAULT_EMOJI
        reactionsCount = typedArray.getInt(
            R.styleable.EmojiView_reactionsCount,
            DEFAULT_REACTION_COUNT
        )
        textPaint.textSize = typedArray.getDimension(
            R.styleable.EmojiView_textSize,
            DEFAULT_TEXT_SIZE_IN_SP.spToPx(context)
        )
        textPaint.color =
            typedArray.getColor(R.styleable.EmojiView_textColor, DEFAULT_TEXT_COLOR)

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(reactionText, 0, reactionText.length, reactionsTextBounds)

        val textWidth = reactionsTextBounds.width()
        val textHeight = reactionsTextBounds.height()

        val sumWidth = textWidth + paddingLeft.orDefaultPadding(PaddingType.LEFT) +
                paddingRight.orDefaultPadding(PaddingType.RIGHT)
        val sumHeight = textHeight + paddingTop.orDefaultPadding(PaddingType.TOP) +
                paddingBottom.orDefaultPadding(PaddingType.BOTTOM)

        val resultWidth = resolveSize(sumWidth, widthMeasureSpec)
        val resultHeight = resolveSize(sumHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        textPoint.x = w / 2f - reactionsTextBounds.width() / 2f
        textPoint.y = h / 2f + reactionsTextBounds.height() / 2f - textPaint.descent()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(reactionText, textPoint.x, textPoint.y, textPaint)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)
        if (isSelected) {
            mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)
        }
        return drawableState
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        isSelected = !isSelected
        reactionsCount = if (isSelected) reactionsCount + 1 else reactionsCount - 1
        return true
    }

    private fun Int.orDefaultPadding(type: PaddingType): Int {
        return if (this == 0) {
            type.defaultValue.dpToPx(context).toInt()
        } else {
            this
        }
    }

    private enum class PaddingType(val defaultValue: Int) {
        RIGHT(DEFAULT_HORIZONTAL_PADDING_IN_DP),
        LEFT(DEFAULT_HORIZONTAL_PADDING_IN_DP),
        TOP(DEFAULT_VERTICAL_PADDING_IN_DP),
        BOTTOM(DEFAULT_VERTICAL_PADDING_IN_DP)
    }

    companion object {

        const val DEFAULT_EMOJI = "\uD83D\uDE00"
        const val DEFAULT_REACTION_COUNT = 0
        private const val DEFAULT_TEXT_SIZE_IN_SP = 12f
        private const val DEFAULT_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_HORIZONTAL_PADDING_IN_DP = 8
        private const val DEFAULT_VERTICAL_PADDING_IN_DP = 6
        private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
    }
}