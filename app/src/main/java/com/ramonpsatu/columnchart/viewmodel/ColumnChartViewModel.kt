package com.ramonpsatu.columnchart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramonpsatu.columnchart.data.repository.PerformanceRepository
import com.ramonpsatu.columnchart.utils.MyCalendar

@Suppress("ArrayInDataClass")
class ColumnChartViewModel(private val repository: PerformanceRepository): ViewModel() {

    data class UiStateColumnChart(var heightValues: Array<Float>, var columnTopValues: Array<String>)

    private val uiStateColumChart: MutableLiveData<UiStateColumnChart> by lazy {
        MutableLiveData<UiStateColumnChart>(
            UiStateColumnChart(heightValues= Array(6){0f},
            columnTopValues = Array(6){"0%"})
        )
    }

    fun stateOnceUiStateColumnChart():LiveData<UiStateColumnChart> = uiStateColumChart


    suspend fun performanceByMonth(numberOfColumns: Int, semester: Int,scale: Float){
        val heights = Array(numberOfColumns) { 0f }
        val columnTopValues = Array(numberOfColumns) { "0%" }
        when(semester){
            1->{
                for (index in heights.indices) {
                    var value :Float
                    if (index > 9){
                        value = repository.performanceByMonth("${MyCalendar.year}-${index + 1}")
                        heights[index] = value*scale
                        columnTopValues[index] = String.format("%.0f",value)+"%"
                    }
                    else {
                        value = repository.performanceByMonth("${MyCalendar.year}-0${index + 1}")
                        heights[index] = value*scale
                        columnTopValues[index] = String.format("%.0f",value)+"%"
                    }
                }
            }
            2->{
                for (index in heights.indices) {
                    var value :Float
                    if (index > 2){
                        value = repository.performanceByMonth("${MyCalendar.year}-${(index + 1)+6}")

                        heights[index] = value *scale
                        columnTopValues[index] = String.format("%.0f",value)+"%"
                    }
                    else {
                        value = repository.performanceByMonth("${MyCalendar.year}-0${(index + 1)+6}")
                        heights[index] = value *scale
                        columnTopValues[index] = String.format("%.0f",value)+"%"
                    }
                }
            }
        }

        uiStateColumChart.postValue(UiStateColumnChart(heights,columnTopValues))
    }

    fun selectSemester():Int{
      return  if (MyCalendar.month in 0..5) 1 else 2
    }
    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: PerformanceRepository) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ColumnChartViewModel(repository) as T
        }


    }

}