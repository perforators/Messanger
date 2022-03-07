package com.krivochkov.homework_2.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.*

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewGroup(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
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

        val width = maxWidthLines + paddingLeft + paddingRight
        val height = totalHeight + paddingTop + paddingBottom

        setMeasuredDimension(
            resolveSize(width, widthMeasureSpec),
            resolveSize(height, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeftPosition = paddingLeft
        var currentTopPosition = paddingTop
        var heightCurrentLine = 0

        children.forEach { child ->
            if (child.visibility != View.GONE) {
                if (currentLeftPosition + child.measuredWidthWithMargins > measuredWidth) {
                    currentLeftPosition = paddingLeft
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

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
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
}
