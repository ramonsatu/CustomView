package com.ramonpsatu.columnchart.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.ramonpsatu.columnchart.data.MockData
import com.ramonpsatu.columnchart.data.repository.PerformanceRepositoryImpl
import com.ramonpsatu.columnchart.databinding.ActivityMainBinding
import com.ramonpsatu.columnchart.utils.MyCalendar
import com.ramonpsatu.columnchart.viewmodel.ColumnChartViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var semester = 0
    private var calendar = Calendar.getInstance()
    private val columnChartViewModel: ColumnChartViewModel by viewModels{
        ColumnChartViewModel.Factory(PerformanceRepositoryImpl(MockData())) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        semester = columnChartViewModel.selectSemester()
        setColumnGraphTitle()
        switchSemesterColumChart()

        columnChartViewModel.stateOnceUiStateColumnChart().observe(this) {
            columnChartViewModel.viewModelScope.launch {
                columnChartViewModel.performanceByMonth(
                    6, semester,
                    binding.columnChartView.scale
                )
            }
            columnChart(it.heightValues, it.columnTopValues)
        }

    }


    private fun columnChart(
        heightsList: Array<Float>,
        columnTopValues: Array<String>
    ) {
        val numberOfColumns = 6
        val numberOfItemsYAxis = 11

        val textsXAxis = Array(numberOfColumns) { "Jan" }

        if (semester == 1) {
            textsXAxis[0] = "Jan"
            textsXAxis[1] = "Feb"
            textsXAxis[2] = "Mar"
            textsXAxis[3] = "Apr"
            textsXAxis[4] = "May"
            textsXAxis[5] = "Jun"
        } else {
            textsXAxis[0] = "Jul"
            textsXAxis[1] = "Aug"
            textsXAxis[2] = "Sep"
            textsXAxis[3] = "Oct"
            textsXAxis[4] = "Nov"
            textsXAxis[5] = "Dec"
        }


        val valueyAxis = Array(numberOfItemsYAxis) { "0%" }
        for (index in valueyAxis.indices) {
            if (index == 0)
                valueyAxis[index] = "0%"
            else
                valueyAxis[index] = "${(index * 10)}%"
        }


        binding.columnChartView.apply {
            alterable = true

            setColumnsHeight(heightsList)
            setTextArrayByYAxis(valueyAxis)
            setTextArrayByXAxis(textsXAxis)
            setValuesArrayByXAxisColumnTop(columnTopValues)
        }

    }

    private fun setColumnGraphTitle() {
        val title =
            "${MyCalendar.year}.$semester - Performance"
        binding.textView.text = title

    }
    private fun switchSemesterColumChart() {
        binding.columnChartView.setOnClickListener {
            semester = if (semester == 1) 2 else 1
            setColumnGraphTitle()
        }

    }
}

