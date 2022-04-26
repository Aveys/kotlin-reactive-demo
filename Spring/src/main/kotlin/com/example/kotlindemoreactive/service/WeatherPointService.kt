package com.example.kotlindemoreactive.service

import com.example.kotlindemoreactive.model.dto.WeatherPointDTO
import com.example.kotlindemoreactive.model.entity.WeatherPoint
import com.example.kotlindemoreactive.model.exceptions.WeatherStationNotFound
import com.example.kotlindemoreactive.repository.WeatherPointRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class WeatherPointService(
    private val weatherPointRepository: WeatherPointRepository,
    private val weatherStationService: WeatherStationService
) {

    fun listAll() = weatherPointRepository.findAllBy().map { it.toDTO() }

    suspend fun saveWeatherPoint(stationId: String, weatherPointDTO: WeatherPointDTO): WeatherPoint {
        return if (weatherStationService.existWeatherStation(stationId)) {
            weatherPointDTO.apply { this.stationId = stationId }
            weatherPointRepository.save(weatherPointDTO.toWeatherPoint())
        } else {
            throw WeatherStationNotFound(stationId)
        }
    }

    suspend fun listByStationId(stationId: String): Flow<WeatherPoint> {
        return if (weatherStationService.existWeatherStation(stationId)) {
            weatherPointRepository.findAll().filter { it.stationId == stationId }
        } else {
            throw WeatherStationNotFound(stationId)
        }
    }
}
