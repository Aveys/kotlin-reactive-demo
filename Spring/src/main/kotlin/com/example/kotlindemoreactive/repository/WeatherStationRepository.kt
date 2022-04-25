package com.example.kotlindemoreactive.repository

import com.example.kotlindemoreactive.model.entity.WeatherStation
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WeatherStationRepository : CoroutineCrudRepository<WeatherStation, String> {
    suspend fun existsWeatherStationByName(name: String): Boolean
}
