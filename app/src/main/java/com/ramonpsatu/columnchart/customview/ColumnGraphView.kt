package com.ramonpsatu.columnchart.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.ramonpsatu.columnchart.R
import kotlin.math.min
/**
 * You must edit the chart in the XML file.
 *
 *  * The space between columns must be greater than the column width by at least 1 dp.
 *  * The number of lines on the y-axis of the graph changes as a result of the number of items on the y-axis.
 *  * The scale of the columns must change in order to merge the space between the lines in the y axis.
 *  * Standard scale is 1 : 4.5f -> Ex: 10% is equals 45f. You must multiply the values of each
 *  index in the height matrix by the
 *  [scale] property so that it exactly matches the screen positions.
 *  * Before calling the functions to change the values in the graph, you need to change
 *  [alterable] to true.
 *  * The size of the arrays corresponding to the heights, the colors of the columns, the texts on the x-axis, the texts at the top of the columns
 *  cannot be different from
 *  [numberOfColumns]. Otherwise an ArrayIndexOutOfBoundsException will be thrown.
 *  * The size of the array corresponding to the y-axis values
 *  cannot be different from
 *  [numberOfItemsYAxis].Otherwise an ArrayIndexOutOfBoundsException will be thrown.
 *
 *  Example code:
 *
 *  fun columnGraph() {
 *
 *           val numberOfColumns = 6
 *           val numberOfItemsYAxis = 11
 *
 *           val heights = Array(numberOfColumns) { 45f }
 *           for (index in heights.indices) {
 *              if (index> 0) heights[index] = heights[index] * (index + 1)
 *           }
 *
 *           val valuesColumnTop = Array(numberOfColumns) { "0%" }
 *           for (index in valuesColumnTop.indices) {
 *               if (index == 0)
 *                   valuesColumnTop[index] = "${1*10}%"
 *               else
 *                   valuesColumnTop[index] = "${(index+1)*10}%"
 *           }
 *
 *          val textsXAxis = Array(numberOfColumns) { "Jan" }
 *
 *          val valueyAxis = Array(numberOfItemsYAxis) { "0%" }
 *          for (index in valueyAxis.indices) {
 *               if (index == 0)
 *                   valueyAxis[index] = "0%"
 *              else
 *                   valueyAxis[index] = "${(index*10)}%"
 *           }
 *
 *          columnGraphView.apply {
 *               alterable = true
 *
 *               setColumnsHeight(heights)
 *               setTextArrayByYAxis(valueyAxis)
 *               setTextArrayByXAxis(textsXAxis)
 *               setValuesArrayByXAxisColumnTop(valuesColumnTop)
 *           }
 *
 *   }
 * @property scale returns the current scale for the y-axis.
 * @property numberOfItemsYAxis Modify the default number of items on the y-axis.
 * @property numberOfColumns Modify the default number of columns.
 * @property xAxisColor Modify the default color of the x-axis.
 * @property yAxisColor Modify the default color of the y-axis.
 * @property backgroundAxisXColor Modify the default colors of the x-axis background lines.
 * @property columnColor Modify the default color of the columns.
 * @property yAxisTextColor Modify the default color of the texts on the y-axis.
 * @property xAxisTextColor Modify the default color of the texts on the x-axis.
 * @property xAxisTextColorColumnTop Modify the default color of texts at the top of the column.
 * @property yAxisTextSize Modify the default text size on the y-axis.
 * @property xAxisTextSize Modify the default text size on the x-axis.
 * @property xAxisTextSizeColumnTop Modify the default text size at the top of the column.
 * @property columnWidth Modify the default column width.
 * @property strokeWidth Modify the default lines width.
 * @property moveYAxisToRight Move the graph's y-axis to the right.
 * @property moveTextsYAxisToRight Move the y-axis texts to the right.
 * @property marginBottomFirstLinexAxis Modify the default value of the margin-bottom of the x-axis.
 * @property marginBottomTextColumnTop Modify the default value of the margin-bottom of texts at the top of the column.
 * @property xAxisStartingPosition Move the graph's x-axis to the right.
 * @property startFirstColumPosition Move the graph's columns to the right.
 * @property spaceBetweenColumns Modify the default value for the space between columns.
 * @property spaceBetweenLinesXAxis Modify the default value for the space between lines on the x-axis.
 * @property expandYAxisBase expands the base of the y-axis.
 * @property expandYAxisTop expands the top of the y-axis.
 *
 *
 *
 * @author Ramon Satu
 */
class ColumnChartView(context: Context, attrs: AttributeSet) : View(context, attrs),
    SetData {

    private val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColumnChartView)
    private var viewWidth = 0
    private var viewHeight = 0
    private var numberOfColumns = 0
    private var xAxisColor = 0
    private var yAxisColor = 0
    private var backgroundAxisXColor = 0
    private var columnColor = 0
    private var strokeWidth = 0f
    private var columnWidth = 0f
    private var columnsHeight = Array(numberOfColumns) { 10f }
    private var columnsColors: Array<Int>? = null
    private var moveYAxisToRight = 0f
    private var numberOfItemsYAxis = 0
    private var textArrayByXAxis = Array(numberOfItemsYAxis) { "Text" }
    private var textArrayByXAxisColumnTop = Array(numberOfItemsYAxis) { "Text" }
    private var textArrayByYAxis = Array(numberOfItemsYAxis) { "Value" }
    private var marginBottomFirstLinexAxis = 0f
    private var marginBottomTextColumnTop = 0f
    private var spaceBetweenLinesXAxis = 0F
    private var spaceBetweenColumns = 0f
    private var xAxisStartingPosition = 0f
    private var startFirstColumPosition = 0f
    private var moveTextsYAxisToRight = 0f
    private var yAxisTextColor = 0
    private var yAxisTextSize = 0f
    private var xAxisTextColor = 0
    private var xAxisTextSize = 0f
    private var xAxisTextColorColumnTop = 0
    private var xAxisTextSizeColumnTop = 0f
    private var expandYAxisBase = 0f
    private var expandYAxisTop = 0f
    private var sscale = 0f

    /**
     * @property scale columns height scale.
     */
    val scale get() = sscale


    /**
     * @property alterable Allows you to change column colors, heights, y-axis values and x-axis texts.
     */
    var alterable = false


    init {
        try {
            xAxisColor = typedArray.getColor(R.styleable.ColumnChartView_xAxis_Color, Color.BLACK)
            yAxisColor = typedArray.getColor(R.styleable.ColumnChartView_yAxis_Color, Color.BLACK)
            backgroundAxisXColor =
                typedArray.getColor(R.styleable.ColumnChartView_background_xAxis_Color, Color.GRAY)
            columnColor =
                typedArray.getColor(R.styleable.ColumnChartView_columnColor, Color.MAGENTA)
            columnWidth = typedArray.getDimension(R.styleable.ColumnChartView_widthColumn, 50f)
            spaceBetweenColumns =
                typedArray.getDimension(R.styleable.ColumnChartView_spaceBetweenColumns, 40f)
            strokeWidth = typedArray.getDimension(R.styleable.ColumnChartView_strokeWidth, 5f)
            startFirstColumPosition =
                typedArray.getDimension(R.styleable.ColumnChartView_startFirstColumPosition, 52f)
            xAxisStartingPosition =
                typedArray.getDimension(R.styleable.ColumnChartView_xAxisStartingPosition, 70f)
            numberOfColumns = typedArray.getInt(R.styleable.ColumnChartView_numberOfColumns, 6)
            numberOfItemsYAxis =
                typedArray.getInt(R.styleable.ColumnChartView_numberOfItemsYAxis, 11)
            moveTextsYAxisToRight =
                typedArray.getDimension(R.styleable.ColumnChartView_moveTextsYAxisToRight, 32f)
            yAxisTextColor =
                typedArray.getColor(R.styleable.ColumnChartView_yAxisTextColor, Color.BLACK)
            yAxisTextSize = typedArray.getDimension(R.styleable.ColumnChartView_yAxisTextSize, 24f)
            xAxisTextColor =
                typedArray.getColor(R.styleable.ColumnChartView_xAxisTextColor, Color.BLACK)
            xAxisTextSize = typedArray.getDimension(R.styleable.ColumnChartView_xAxisTextSize, 24f)
            marginBottomFirstLinexAxis =
                typedArray.getDimension(R.styleable.ColumnChartView_marginBottomFirstLine, 40f)
            spaceBetweenLinesXAxis =
                typedArray.getDimension(R.styleable.ColumnChartView_spaceBetweenLinesXAxis, 45f)
            moveYAxisToRight =
                typedArray.getDimension(R.styleable.ColumnChartView_moveYAxisToRight, 0f)
            xAxisTextColorColumnTop = typedArray.getColor(
                R.styleable.ColumnChartView_xAxisTextColorColumnTop,
                Color.BLACK
            )
            xAxisTextSizeColumnTop =
                typedArray.getDimension(R.styleable.ColumnChartView_xAxisTextSizeColumnTop, 24f)

            expandYAxisBase = typedArray.getDimension(R.styleable.ColumnChartView_expandYAxisBase, 0f)
            expandYAxisTop = typedArray.getDimension(R.styleable.ColumnChartView_expandYAxisTop, 0f)

        } finally {
            typedArray.recycle()
        }
    }

    private var cPaint: Paint? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        cPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        cPaint?.style = Paint.Style.FILL
        cPaint!!.strokeWidth = strokeWidth
        sscale = (height - (spaceBetweenLinesXAxis * (numberOfItemsYAxis - 1))) / -100f
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cPaint = null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when (layoutParams.width) {
            ViewGroup.LayoutParams.WRAP_CONTENT -> {
                val r = resources
                val value = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 252f, r.displayMetrics
                )
                viewWidth = value.toInt()
            }

            ViewGroup.LayoutParams.MATCH_PARENT -> {
                viewWidth = min(
                    MeasureSpec.getSize(widthMeasureSpec),
                    MeasureSpec.getSize(heightMeasureSpec)
                )

            }

            else -> {
                viewWidth = widthMeasureSpec
            }
        }

        when (layoutParams.height) {
            ViewGroup.LayoutParams.WRAP_CONTENT -> {
                val r = resources
                val value = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 212f, r.displayMetrics
                )
                viewHeight = value.toInt()
            }

            ViewGroup.LayoutParams.MATCH_PARENT -> {
                viewHeight = min(
                    MeasureSpec.getSize(widthMeasureSpec),
                    MeasureSpec.getSize(heightMeasureSpec)
                )
            }

            else -> {
                viewHeight = heightMeasureSpec
            }
        }
        setMeasuredDimension(viewWidth, viewHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //Lines of the X Axis move up y Axis
        for (index in 0 until numberOfItemsYAxis) {
            when (index) {
                0 -> {
                    cPaint!!.color = xAxisColor
                    canvas.drawLine(
                        xAxisStartingPosition,
                        height - strokeWidth - marginBottomFirstLinexAxis,
                        width.toFloat(),
                        height - strokeWidth - marginBottomFirstLinexAxis,
                        cPaint!!
                    )
                }

                in 1 until numberOfItemsYAxis -> {
                    cPaint!!.color = backgroundAxisXColor
                    canvas.drawLine(
                        xAxisStartingPosition,
                        height - (spaceBetweenLinesXAxis * index) - marginBottomFirstLinexAxis,
                        width.toFloat(),
                        height - (spaceBetweenLinesXAxis * index) - marginBottomFirstLinexAxis,
                        cPaint!!
                    )
                }
            }
        }

        //Y Axis
        cPaint!!.color = yAxisColor
        canvas.drawLine(
            xAxisStartingPosition + 10 + moveYAxisToRight,
            height - (15f + (spaceBetweenLinesXAxis * numberOfItemsYAxis)) - expandYAxisTop,
            xAxisStartingPosition + 10 + moveYAxisToRight,
            height - strokeWidth - marginBottomFirstLinexAxis + 10 + expandYAxisBase,
            cPaint!!
        )


        //Y Axis Texts
        cPaint!!.color = yAxisTextColor
        cPaint!!.textSize = yAxisTextSize
        cPaint!!.textAlign = Paint.Align.CENTER
        if (alterable) {
            drawTextsByAxisY(canvas, cPaint!!, height.toFloat(), spaceBetweenLinesXAxis.toInt())
        } else {
            drawTextsByAxisYDefaultValues(
                canvas,
                cPaint!!,
                height.toFloat(),
                spaceBetweenLinesXAxis.toInt()
            )

        }

        //Axis X Texts
        cPaint!!.color = xAxisTextColor
        cPaint!!.textSize = xAxisTextSize
        if (alterable) {
            drawTextsByAxisX(canvas, cPaint!!, height.toFloat())
            drawTextsByColumnTop(canvas, cPaint!!, height.toFloat())
        } else {
            drawTextsByAxisXDefaultValues(canvas, cPaint!!, height.toFloat())
            drawTextsByColumnTopDefaultValues(canvas, cPaint!!, height.toFloat())
        }

        //Columns
        cPaint!!.color = columnColor
        cPaint!!.style = Paint.Style.FILL
        cPaint!!.flags = Paint.LINEAR_TEXT_FLAG or Paint.SUBPIXEL_TEXT_FLAG
        if (alterable) {
            drawColumns(canvas, cPaint!!, height.toFloat())
        } else {
            drawColumnsDefaultValues(canvas, cPaint!!, height.toFloat())
        }
    }

    private fun drawTextsByAxisYDefaultValues(
        canvas: Canvas,
        paint: Paint,
        height: Float,
        constValueAxisXMoveUp: Int
    ) {
        for (index in 0 until numberOfItemsYAxis) {
            when (index) {
                0 -> {
                    canvas.drawText(
                        "Value",
                        moveTextsYAxisToRight,
                        height - 5 - marginBottomFirstLinexAxis,
                        paint
                    )
                }

                in 1 until numberOfItemsYAxis -> {

                    canvas.drawText(
                        "Value",
                        moveTextsYAxisToRight,
                        height - (constValueAxisXMoveUp * index) - marginBottomFirstLinexAxis,
                        paint
                    )
                }
            }
        }
    }

    private fun drawTextsByAxisY(
        canvas: Canvas,
        paint: Paint,
        height: Float,
        constValueAxisXMoveUp: Int
    ) {
        try {
            for (index in 0 until numberOfItemsYAxis) {
                val string = textArrayByYAxis
                when (index) {
                    0 -> {
                        canvas.drawText(
                            string[index],
                            moveTextsYAxisToRight,
                            height - 5 - marginBottomFirstLinexAxis,
                            paint
                        )
                    }

                    in 1 until numberOfItemsYAxis -> {
                        canvas.drawText(
                            string[index],
                            moveTextsYAxisToRight,
                            height - (constValueAxisXMoveUp * index) - marginBottomFirstLinexAxis,
                            paint
                        )
                    }
                }
            }
        } catch (ex: Exception) {
            throw ArrayIndexOutOfBoundsException("The size of the array cannot be different of the number of items on the y-axis.")
        }


    }

    private fun drawTextsByAxisXDefaultValues(canvas: Canvas, paint: Paint, height: Float) {

        for (index in 0 until numberOfColumns) {
            canvas.drawText(
                "Text",
                startFirstColumPosition + (50 * index) + xAxisStartingPosition + (spaceBetweenColumns * index) + columnWidth / 2,
                height - 10,
                paint
            )
        }
    }

    private fun drawTextsByAxisX(canvas: Canvas, paint: Paint, height: Float) {
        try {
            val string = textArrayByXAxis
            for (index in 0 until numberOfColumns) {


                canvas.drawText(
                    string[index],
                    startFirstColumPosition + 50 * index + xAxisStartingPosition + spaceBetweenColumns * index + columnWidth / 2,
                    height - 10,
                    paint
                )

            }
        } catch (ex: Exception) {
            throw ArrayIndexOutOfBoundsException("The size of the text array cannot be different of the number of columns.")
        }

    }

    private fun drawTextsByColumnTopDefaultValues(canvas: Canvas, paint: Paint, height: Float) {
        paint.color = xAxisTextColorColumnTop
        paint.textSize = xAxisTextSizeColumnTop

        for (index in 0 until numberOfColumns) {


            canvas.drawText(
                "Text",
                startFirstColumPosition + (50 * index) + xAxisStartingPosition + (spaceBetweenColumns * index) + (columnWidth / 2),
                height - (spaceBetweenLinesXAxis * numberOfItemsYAxis),
                paint
            )


        }
    }

    private fun drawTextsByColumnTop(canvas: Canvas, paint: Paint, height: Float) {
        try {
            paint.color = xAxisTextColorColumnTop
            paint.textSize = xAxisTextSizeColumnTop
            for (index in 0 until numberOfColumns) {

                canvas.drawText(
                    textArrayByXAxisColumnTop[index],
                    startFirstColumPosition + (50 * index) + xAxisStartingPosition + (spaceBetweenColumns * index) + (columnWidth / 2),
                    calculateColumnHeight(height, columnsHeight[index]) - 10,
                    paint
                )


            }
        } catch (ex: Exception) {
            throw ArrayIndexOutOfBoundsException("The size of the height array cannot be different of the number of columns.")
        }


    }

    private fun drawColumnsDefaultValues(canvas: Canvas, paint: Paint, height: Float) {

        for (index in 0 until numberOfColumns) {

            if ( index in 0 until numberOfColumns){
                canvas.drawRect(
                    startFirstColumPosition   + (50 * index) + xAxisStartingPosition + spaceBetweenColumns * index,
                    height - (spaceBetweenLinesXAxis * numberOfItemsYAxis) + (2 * strokeWidth) / 2,
                    startFirstColumPosition  +(50 * index) + columnWidth + xAxisStartingPosition + spaceBetweenColumns * index,
                    height - strokeWidth - marginBottomFirstLinexAxis,
                    paint
                )
            }


        }
    }

    private fun drawColumns(canvas: Canvas, paint: Paint, height: Float) {

        try {
            for (index in 0 until numberOfColumns) {
                if (columnsColors != null) {
                    paint.color = columnsColors!![index]
                }

                canvas.drawRect(
                    xAxisStartingPosition + (50 * index) + startFirstColumPosition + spaceBetweenColumns * index,
                    calculateColumnHeight(height, columnsHeight[index]),
                    xAxisStartingPosition + (50 * index) + columnWidth + startFirstColumPosition + spaceBetweenColumns * index,
                    height - strokeWidth - marginBottomFirstLinexAxis,
                    paint
                )

            }
        } catch (ex: Exception) {
            throw ArrayIndexOutOfBoundsException("The size of the height array cannot be different of the number of columns.")
        }


    }

    private fun calculateColumnHeight(viewHeight: Float, columnHeight: Float): Float {
        return (viewHeight - marginBottomFirstLinexAxis - columnHeight)
    }

    /**
     * Assigns the values corresponding to the height of each column.
     *@param heightArray a float array.
     */
    override fun setColumnsHeight(heightArray: Array<Float>) {
        this.columnsHeight = heightArray
        invalidate()
        requestLayout()
    }

    /**
     * Assigns the corresponding texts to the base of the x-axis.
     *@param textArrayXAxis a string array.
     */
    override fun setTextArrayByXAxis(textArrayXAxis: Array<String>) {
        this.textArrayByXAxis = textArrayXAxis
    }

    /**
     * Assigns the corresponding texts to the values on the y-axis.
     *@param textArrayYAxis a string array.
     */
    override fun setTextArrayByYAxis(textArrayYAxis: Array<String>) {
        this.textArrayByYAxis = textArrayYAxis
    }

    /**
     * Assigns the corresponding texts to the values of the column tops.
     *@param valuesArrayColumnTop a string array.
     */
    override fun setValuesArrayByXAxisColumnTop(valuesArrayColumnTop: Array<String>) {
        this.textArrayByXAxisColumnTop = valuesArrayColumnTop

    }

    /**
     * Assigns the colors corresponding to the column values.
     *@param colorArray an integer array.
     */
    override fun setColumnColors(colorArray: Array<Int>) {
        this.columnsColors = colorArray
    }

}