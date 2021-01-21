package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var buttonColor = 0
    private var loadingColor = 0
    private var label = ""
    private var loadingText = ""
    private var textColor = 0
    private var progress = 0f
    private var angle = 0f

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Loading -> {
                valueAnimator.duration = 2000
                valueAnimator.interpolator = LinearInterpolator()
                valueAnimator.setFloatValues(0.0f, width.toFloat())
                valueAnimator.repeatCount = ValueAnimator.INFINITE
                valueAnimator.addUpdateListener {
                    progress = it.animatedValue as Float
                    angle = (progress / widthSize)*360
                    invalidate()
                }
                valueAnimator.start()
            }
            ButtonState.Completed -> invalidate()
        }

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    private fun drawButton(canvas: Canvas) {
        paint.color = buttonColor
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
        paint.color = textColor
        if (buttonState == ButtonState.Completed) {
            canvas.drawText(label, 0, label.length, widthSize.toFloat()/2, (heightSize.toFloat() + 16)/2, paint)
        }
    }

    private fun drawAnimatingButton(canvas: Canvas) {
        paint.color = loadingColor
        canvas.drawRect(0f, 0f, progress, heightSize.toFloat(), paint)
        drawAnimatingCircle(canvas)
        paint.color = textColor
        if (buttonState == ButtonState.Loading){
            canvas.drawText(loadingText, 0, loadingText.length, widthSize.toFloat()/2, (heightSize.toFloat() + 16)/2, paint)
        }
    }

    private fun drawAnimatingCircle(canvas: Canvas) {
        paint.color = Color.YELLOW
        canvas.drawArc((widthSize - 100f),
                (heightSize - 50f) / 2,
                (widthSize - 50f),
                (heightSize + 50f) / 2,
                0F, angle,
                true, paint)
    }


    init {
        buttonState = ButtonState.Completed
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonColor = getColor(R.styleable.LoadingButton_buttonColor, 0)
            label = getString(R.styleable.LoadingButton_text).toString()
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            loadingColor = getColor(R.styleable.LoadingButton_loadingColor, 0)
            loadingText = getString(R.styleable.LoadingButton_loadingText).toString()
        }

    }

    override fun performClick(): Boolean {
        buttonState = ButtonState.Loading
        invalidate()
        return super.performClick()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawButton(canvas)
        drawAnimatingButton(canvas)

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}