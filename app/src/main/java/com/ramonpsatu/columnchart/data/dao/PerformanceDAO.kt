package com.ramonpsatu.columnchart.data.dao

interface PerformanceDAO{

    suspend fun performanceByMonth(yearAndMonth:String): Float

    suspend fun countPerformanceByMonth(yearAndMonth:String): Int

}