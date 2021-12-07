package com.example.customviewexample.view

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import com.example.customviewexample.R
import kotlin.math.min
import kotlin.math.roundToInt

const val MAX_COUNT = 9999

class CustomCounter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textRect = Rect()
    private val gestureDetector: GestureDetector

    var count: Int = 0
        set(value) {
            field = min(value, MAX_COUNT)
            countText = field.toString()
            invalidate()
        }

    var circleColor: Int = Color.BLUE
        set(value) {
            field = value
            backgroundPaint.color = value
            invalidate()
        }

    private var countText = ""

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomCounter, 0, 0)

        // get attributes from typed array
        val counterTextSize = typedArray.getDimensionPixelSize(
            R.styleable.CustomCounter_textSize,
            (64f * resources.displayMetrics.scaledDensity).roundToInt()
        ).toFloat()

        val textColor = typedArray.getColor(R.styleable.CustomCounter_textColor, Color.WHITE)
        circleColor = typedArray.getColor(R.styleable.CustomCounter_circleColor, Color.BLUE)

        // setup paint for drawing text
        textPaint.apply {
            color = textColor
            textSize = counterTextSize
        }

        typedArray.recycle()

        // Initial value of counter
        count = 0

        // Init gesture detector
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onLongPress(e: MotionEvent?) {
                reset()
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        // calculate center
        val centerX = width * 0.5f
        val centerY = height * 0.5f

        // draw circle background
        canvas.drawCircle(centerX, centerY, centerX, backgroundPaint)

        // extract text bounds
        textPaint.getTextBounds(countText, 0, countText.length, textRect)
        val textWidth = textPaint.measureText(countText)
        val textHeight = textRect.height()

        // draw text
        canvas.drawText(
            countText,
            centerX - textWidth / 2,
            centerY + textHeight / 2,
            textPaint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // get max text size
        val maxTextWidth = textPaint.measureText(MAX_COUNT.toString())

        // get desired view sizes
        val desiredWidth = (maxTextWidth + paddingLeft.toFloat() + paddingRight.toFloat()).roundToInt()
        val desiredHeight = (maxTextWidth + paddingTop.toFloat() + paddingBottom.toFloat()).roundToInt()

        // calculate actual sizes
        val measuredWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val measuredHeight = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    fun reset() {
        animatedReset()
    }

    fun increment() {
        count++
    }

    private fun animatedReset() {
        // change count animation
        val countAnimator = ValueAnimator.ofInt(count, 0).apply {
            duration = 2000
            interpolator = BounceInterpolator()
            addUpdateListener { animator ->
                count = animator.animatedValue as Int
            }
        }

        // change color animation
        val colorAnimator = ObjectAnimator.ofObject(this, "circleColor", ArgbEvaluator(), Color.RED).apply {
            duration = 2000
        }

        AnimatorSet().apply {
            playTogether(countAnimator, colorAnimator)
            start()
        }
    }
}
