package com.dsphoenix.soundvault.widgets.bubblepicker

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.withStyledAttributes
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.utils.TAG
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class BubblePicker @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private lateinit var bubbles: List<Bubble>

    private var bubbleRadius = 0f
    private var placementRadius = 0f
    private val pointPosition = PointF(0f, 0f)

    // attrs
    private var activePos = 0
    private var bubblesCount = 0
    private var inactiveRadius = 0f
    private var activeRadius = 0f
    private var inactiveColor = 0
    private var activeColor = 0
    private var inactiveIconSize = 0
    private var activeIconSize = 0
    private var inactiveIconColor = 0
    private var activeIconColor = 0
    lateinit var drawables: List<Drawable?>

    private lateinit var onActiveChangeListener: (Int) -> Boolean

    init {
        Log.d(TAG, "Init called")
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.BubblePicker) {
            activePos = getInt(R.styleable.BubblePicker_defaultActive, 1) - 1
            bubblesCount = getInt(R.styleable.BubblePicker_bubblesCount, 4)
            bubbles = List(bubblesCount) { Bubble() }
            inactiveColor = getColor(R.styleable.BubblePicker_inactiveColor, 0)
            activeColor = getColor(R.styleable.BubblePicker_activeColor, 0)
            inactiveRadius =
                getDimensionPixelSize(R.styleable.BubblePicker_inactiveRadius, 100).toFloat()
            activeRadius =
                getDimensionPixelSize(R.styleable.BubblePicker_activeRadius, 120).toFloat()
            val iconMultiplier = activeRadius / inactiveRadius
            inactiveIconSize =
                getDimensionPixelSize(R.styleable.BubblePicker_inactiveIconSize, 100)
            activeIconSize =
                getDimensionPixelSize(
                    R.styleable.BubblePicker_activeIconSize,
                    (inactiveIconSize * iconMultiplier).toInt()
                )
            inactiveIconColor = getColor(R.styleable.BubblePicker_inactiveIconColor, 0)
            activeIconColor = getColor(R.styleable.BubblePicker_activeIconColor, 0)
            drawables = listOf(
                getDrawable(R.styleable.BubblePicker_drawableIcon1),
                getDrawable(R.styleable.BubblePicker_drawableIcon2),
                getDrawable(R.styleable.BubblePicker_drawableIcon3),
                getDrawable(R.styleable.BubblePicker_drawableIcon4),
                getDrawable(R.styleable.BubblePicker_drawableIcon5)
            )
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        placementRadius = (min(w, h * 2) / 2 * 0.75).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        bubbles = bubbles.mapIndexed { i, bubble ->
            pointPosition.computePositionXY(i, placementRadius)
            if (i == activePos) {
                bubbleRadius = activeRadius
                paint.color = activeColor
                drawables[i]?.bounds = getRect(pointPosition, activeIconSize)
                drawables[i]?.setTint(activeIconColor)
            } else {
                bubbleRadius = inactiveRadius
                paint.color = inactiveColor
                drawables[i]?.bounds = getRect(pointPosition, inactiveIconSize)
                drawables[i]?.setTint(inactiveIconColor)
            }
            canvas.drawCircle(pointPosition.x, pointPosition.y, bubbleRadius, paint)
            paint.color = Color.BLACK
            drawables[i]?.draw(canvas)
            bubble.copy(centerX = pointPosition.x, centerY = pointPosition.y, radius = bubbleRadius)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val newActivePos = bubbles.indexOfFirst { it.isInArea(event.x, event.y) }
                if (newActivePos == -1) return false
                activePos = newActivePos
                Log.d(TAG, "new activePos = $activePos")
                invalidate()
                if (this::onActiveChangeListener.isInitialized) {
                    return onActiveChangeListener(activePos)
                }
                return false
            }
        }
        return false
    }

    fun setOnActiveChangeListener(listener: (Int) -> Boolean) {
        onActiveChangeListener = listener
    }

    private fun PointF.computePositionXY(pos: Int, radius: Float) {
        val angleDiff = (Math.PI / (bubblesCount))
        val startAngle = Math.PI - angleDiff / 2
        val angle = startAngle + (pos + 1) * angleDiff
        x = (radius * cos(angle)).toFloat() + width / 2
        y = (radius * sin(angle)).toFloat() + height
    }

    private fun getRect(centerPoint: PointF, size: Int): Rect {
        return Rect(
            (centerPoint.x - size / 2).toInt(),
            (centerPoint.y - size / 2).toInt(),
            (centerPoint.x + size / 2).toInt(),
            (centerPoint.y + size / 2).toInt()
        )
    }
}
