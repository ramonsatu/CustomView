package com.ramonpsatu.columnchart.data

import com.ramonpsatu.columnchart.data.dao.PerformanceDAO
import com.ramonpsatu.columnchart.model.Performance
import java.util.UUID

class MockData : PerformanceDAO {

    private val  data = mutableListOf(
        Performance(
            id = UUID.randomUUID().toString(),
            performance = 75.3f,
            yearMonth = "2023-01"
        ),
        Performance(
            id = UUID.randomUUID().toString(),
            performance = 55.5f,
            yearMonth = "2023-03"
        ),
        Performance(
            id = UUID.randomUUID().toString(),
            performance = 85.5f,
            yearMonth = "2023-04"
        ),
        Performance(
            id = UUID.randomUUID().toString(),
            performance = 65.5f,
            yearMonth = "2023-06"
        ),
        Performance(
            id = UUID.randomUUID().toString(),
            performance = 95.5f,
            yearMonth = "2023-07"
        ),
        Performance(
            id = UUID.randomUUID().toString(),
            performance = 45.5f,
            yearMonth = "2023-08"
        ),
        Performance(
            id = UUID.randomUUID().toString(),
            performance = 82.55f,
            yearMonth = "2023-10"
        ),
        Performance(
            id = UUID.randomUUID().toString(),
            performance = 64.8f,
            yearMonth = "2023-12"
        )
    )


    override suspend fun performanceByMonth(yearAndMonth: String): Float {

        var restul = 0f

        data.forEach {
            if (it.yearMonth == yearAndMonth){
                restul += it.performance
            }
        }

        return restul
    }

    override suspend fun countPerformanceByMonth(yearAndMonth: String): Int {

        var restul = 0

        data.forEach {
            if (it.yearMonth == yearAndMonth
                && it.performance > 0){
                restul += 1
            }
        }
        return restul
    }
}