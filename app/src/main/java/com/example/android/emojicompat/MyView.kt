package com.example.android.emojicompat

import android.content.Context
import android.graphics.Canvas
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class MyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var layout: StaticLayout? = null
    private val paint = TextPaint().apply {
        textSize = 20f * resources.displayMetrics.scaledDensity
    }

    fun setText(s: CharSequence) {
        layout = StaticLayout(
            s,
            paint,
            Layout.getDesiredWidth(s, paint).toInt(),
            Layout.Alignment.ALIGN_NORMAL,
            0f,
            0f,
            false
        )
        requestLayout()
//        layout = StaticLayout.Builder.obtain(
//            s, 0, s.length, paint, StaticLayout.getDesiredWidth(s, paint).toInt()
//        ).build()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        layout?.apply {
            setMeasuredDimension(width, height)
        } ?: setMeasuredDimension(0, 0)
    }

    override fun onDraw(canvas: Canvas) {
        layout?.draw(canvas)
    }
}