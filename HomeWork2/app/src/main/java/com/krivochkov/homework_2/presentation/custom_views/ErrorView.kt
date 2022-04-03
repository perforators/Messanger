package com.krivochkov.homework_2.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.krivochkov.homework_2.R

class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    var text: String = ""
        set(value) {
            field = value
            errorText.text = value
        }

    private var onErrorButtonClick: (View) -> Unit = {  }

    private val errorText: TextView
    private val errorButton: Button
    private val errorImage: ImageView

    init {
        inflate(context, R.layout.error_layout, this)

        errorText = findViewById(R.id.error_text)
        errorButton = findViewById(R.id.error_button)
        errorImage = findViewById(R.id.error_image)

        errorButton.setOnClickListener {
            onErrorButtonClick(it)
        }
    }

    fun setOnErrorButtonClickListener(listener: (View) -> Unit) {
        onErrorButtonClick = listener
    }
}