package com.example.kotlindemoreactive.service

import com.example.kotlindemoreactive.model.entity.WeatherStation
import com.example.kotlindemoreactive.repository.WeatherStationRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class WeatherStationService(private val weatherStationRepository: WeatherStationRepository) {

    fun saveWeatherStation(weatherStation: WeatherStation): Mono<WeatherStation> {
        return weatherStationRepository.save(weatherStation)
    }

    fun listAllWeatherStations(): Flux<WeatherStation> {
        return weatherStationRepository.findAll()
    }

    fun existWeatherStation(name: String): Mono<Boolean> {
        return weatherStationRepository.existsWeatherStationByName(name)
    }
}
