package com.example.kotlindemoreactive.service

import com.example.kotlindemoreactive.model.entity.WeatherStation
import com.example.kotlindemoreactive.repository.WeatherStationRepository
import org.springframework.stereotype.Service

@Service
class WeatherStationService(private val weatherStationRepository: WeatherStationRepository) {

    suspend fun saveWeatherStation(weatherStation: WeatherStation): WeatherStation =
        weatherStationRepository.save(weatherStation)

    suspend fun listAllWeatherStations() = weatherStationRepository.findAll()

    suspend fun existWeatherStation(name: String) = weatherStationRepository.existsWeatherStationByName(name)

    fun getAll() = weatherStationRepository.findAll()
}
