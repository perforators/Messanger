package com.krivochkov.homework_2.presentation.custom_views

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

    private var onChangedSelect: (EmojiView, Boolean) -> Unit = { _, _ -> }

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
            resources.getDimension(R.dimen.emoji_text_size)
        )
        textPaint.color = typedArray.getColor(
            R.styleable.EmojiView_textColor,
            context.getColor(R.color.emoji_text)
        )

        typedArray.recycle()
    }

    fun setOnChangedSelectListener(onChangedSelect: (EmojiView, Boolean) -> Unit) {
        this.onChangedSelect = onChangedSelect
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(reactionText, 0, reactionText.length, reactionsTextBounds)

        val textWidth = reactionsTextBounds.width()
        val textHeight = reactionsTextBounds.height()

        val sumWidth = textWidth + paddingLeft.orDefaultPadding() + paddingRight.orDefaultPadding()
        val sumHeight = textHeight + paddingTop.orDefaultPadding() + paddingBottom.orDefaultPadding()

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
        val drawableState = super.onCreateDrawableState(
            extraSpace + SUPPORTED_DRAWABLE_STATE.size
        )
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
        onChangedSelect(this, isSelected)
        return true
    }

    private fun Int.orDefaultPadding(): Int {
        return if (this == 0) {
            resources.getDimension(R.dimen.middle_padding).toInt()
        } else {
            this
        }
    }

    companion object {

        const val DEFAULT_EMOJI = "\uD83D\uDE00"
        const val DEFAULT_REACTION_COUNT = 1
        private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
    }
}