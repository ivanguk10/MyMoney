package com.example.mymoney.view

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.mymoney.R

class PieChart(context: Context, attrs: AttributeSet): View(context, attrs) {


    private var textHeight = 0f
    private var exp1 = 0f
    private var exp2 = 0f
    private var exp3 = 0f
    private var exp4 = 0f
    private var exp5 = 0f
    private var exp6 = 0f


    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PieChart,
            0, 0).apply {

            try {
                exp1 = getFloat(R.styleable.PieChart_expense1, 0f)
                exp2 = getFloat(R.styleable.PieChart_expense2, 0f)
                exp3 = getFloat(R.styleable.PieChart_expense3, 0f)
                exp4 = getFloat(R.styleable.PieChart_expense4, 0f)
                exp5 = getFloat(R.styleable.PieChart_expense5, 0f)
            } finally {
                recycle()
            }
        }
    }

    fun setExp1(value: Float) {
        exp1 = value
        invalidate()
        requestLayout()
    }
    fun setExp2(value: Float) {
        exp2 = value
        invalidate()
        requestLayout()
    }
    fun setExp3(value: Float) {
        exp3 = value
        invalidate()
        requestLayout()
    }
    fun setExp4(value: Float) {
        exp4 = value
        invalidate()
        requestLayout()
    }
    fun setExp5(value: Float) {
        exp5 = value
        invalidate()
        requestLayout()
    }
    fun setExp6(value: Float) {
        exp6 = value
        invalidate()
        requestLayout()
    }

    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.black)
        if (textHeight == 0f) {
            textHeight = textSize
        } else {
            textSize = textHeight
        }
    }

    private val piePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20F
        color = ContextCompat.getColor(context, R.color.lightGray)
    }

    private val expense1 = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 30F
        color = ContextCompat.getColor(context, R.color.darkestPurple)
    }

    private val oval1 = RectF()
    private var centerX = 0f
    private var centerY = 0f


    private val expense2 = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 30F
        color = ContextCompat.getColor(context, R.color.purpleDark)
    }

    private val expense3 = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 30F
        color = ContextCompat.getColor(context, R.color.pastelBlue)
    }

    private val expense4 = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 30F
        color = ContextCompat.getColor(context, R.color.pastelGreen)
    }

    private val expense5 = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 30F
        color = ContextCompat.getColor(context, R.color.pastel)
    }

    private val expense6 = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 30F
        color = ContextCompat.getColor(context, R.color.pastelOrange)
    }


    override fun onDraw(canvas: Canvas?) {
        val x = width.toFloat()
        val y = height.toFloat()
        val radius = (height / 2.25).toFloat()
        canvas?.drawCircle(x/2, y/2, radius, piePaint)

        centerX = x / 2;
        centerY = y / 2;
        oval1.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        val expStart = -90f

        val exp2Start = exp1 - 90f

        val exp3Start = exp1 + exp2 - 90f

        val exp4Start = exp1 + exp2 + exp3 - 90f

        val exp5Start = exp1 + exp2 + exp3 + exp4 - 90f

        val exp6Start = exp1 + exp2 + exp3 + exp4 + exp5 - 90f

        canvas?.drawArc(oval1, expStart, exp1, false, expense1)

        canvas?.drawArc(oval1, exp2Start, exp2, false, expense2)

        canvas?.drawArc(oval1, exp3Start, exp3, false, expense3)

        canvas?.drawArc(oval1, exp4Start, exp4, false, expense4)

        canvas?.drawArc(oval1, exp5Start, exp5, false, expense5)

        canvas?.drawArc(oval1, exp6Start, exp6, false, expense6)

        super.onDraw(canvas)
    }


}