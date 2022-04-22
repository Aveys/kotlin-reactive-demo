package com.example.kotlindemoreactive.service

import com.example.kotlindemoreactive.model.dto.WeatherPointDTO
import com.example.kotlindemoreactive.model.entity.WeatherPoint
import com.example.kotlindemoreactive.repository.WeatherPointRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class WeatherPointService(
    private val weatherPointRepository: WeatherPointRepository,
    private val weatherStationService: WeatherStationService
) {

    fun listAll() = weatherPointRepository.findAll()

    fun saveWeatherPoint(stationId: String, weatherPointDTO: WeatherPointDTO): Mono<WeatherPoint> {
        return weatherStationService.existWeatherStation(stationId)
            .flatMap {
                weatherPointDTO.apply { this.stationId = stationId }
                return@flatMap if (it) {
                    weatherPointRepository.save(weatherPointDTO.toWeatherPoint())
                } else {
                    Mono.error(Exception("Weather station not found"))
                }
            }
    }

    fun listByStationId(stationId: String) =
        weatherStationService.existWeatherStation(stationId).map {
            if (!it) {
                throw Exception("Weather station not found")
            }
        }
            .thenMany(weatherPointRepository.findAll())
            .filter { it.stationId == stationId }
}
