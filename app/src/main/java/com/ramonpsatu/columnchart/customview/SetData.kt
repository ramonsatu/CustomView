package com.ramonpsatu.columnchart.customview


sealed interface SetData{

    fun setColumnsHeight(heightArray: Array<Float>)

    fun setTextArrayByXAxis(textArrayXAxis: Array<String>)

    fun setTextArrayByYAxis(textArrayYAxis: Array<String>)

    fun setValuesArrayByXAxisColumnTop(valuesArrayColumnTop: Array<String>)

    fun setColumnColors(colorArray: Array<Int>)

}