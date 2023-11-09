package com.ramonpsatu.columnchart.data.repository


interface PerformanceRepository {

    suspend fun performanceByMonth(yearAndMonth:String): Float

    suspend fun countPerformanceByMonth(yearAndMonth:String): Int


}