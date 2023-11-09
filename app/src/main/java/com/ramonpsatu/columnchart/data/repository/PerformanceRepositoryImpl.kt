package com.ramonpsatu.columnchart.data.repository

import com.ramonpsatu.columnchart.data.dao.PerformanceDAO


class PerformanceRepositoryImpl(private val performanceDAO: PerformanceDAO) : PerformanceRepository {

    private companion object {

        private const val LOG_TAG = "PerformanceRepository"
    }

    override suspend fun performanceByMonth(yearAndMonth: String): Float {
        val divisor= countPerformanceByMonth(yearAndMonth)
        return if (divisor > 0 ){
            performanceDAO.performanceByMonth(yearAndMonth) / countPerformanceByMonth(yearAndMonth)

        }else{
            return 0f
        }
    }

    override suspend fun countPerformanceByMonth(yearAndMonth: String): Int {
        return performanceDAO.countPerformanceByMonth(yearAndMonth)
    }

}