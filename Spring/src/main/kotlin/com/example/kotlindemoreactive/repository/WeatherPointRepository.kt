package com.example.kotlindemoreactive.repository

import com.example.kotlindemoreactive.model.entity.WeatherPoint
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.repository.Tailable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WeatherPointRepository : CoroutineCrudRepository<WeatherPoint, String>{
    @Tailable
    fun findAllBy(): Flow<WeatherPoint>
}
