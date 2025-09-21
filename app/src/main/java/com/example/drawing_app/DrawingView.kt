package com.example.drawing_app

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import androidx.core.graphics.createBitmap

class DrawingView(
    context: Context?, attrs: AttributeSet?
) : View(context, attrs) {

    var bitmap: Bitmap? = null
    var paint: Paint? = null
    var path: Path? = null

    var canvas: Canvas? = null
    var mX: Float? = null
    var mY: Float? = null

    companion object {
        const val TOLERANCE = 4
    }

    init {
        paint = Paint()
        path = Path()

        paint?.isAntiAlias = true
        paint?.setColor(Color.BLACK)
        paint?.style = Paint.Style.STROKE
        paint?.strokeJoin = Paint.Join.ROUND
        paint?.strokeWidth = 8f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bitmap = createBitmap(w, h)
        canvas = Canvas(bitmap!!)
        canvas?.setBitmap(bitmap)
        this.draw(canvas!!)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap!!, 0f,0f,null)
        canvas.drawPath(path!!,paint!!)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val x = event?.x
        val y = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startTouch(x!!,y!!)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                moveTouch(x!!,y!!)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                upTouch()
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun upTouch() {
        path?.lineTo(mX!!, mY!!)
    }

    private fun moveTouch(x: Float, y: Float) {
        val dx = abs(x - mX!!)
        val dy = abs(y - mY!!)

        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            path?.quadTo(mX!!, mY!!, (x+mX!!)/2, (y+mY!!)/2)
            mX = x
            mY = y
        }
    }

    private fun startTouch(x: Float, y: Float) {
        path?.moveTo(x,y)
        mX = x
        mY = y
    }
}