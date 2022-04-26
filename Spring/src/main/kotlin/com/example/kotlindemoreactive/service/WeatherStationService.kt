package com.example.kotlindemoreactive.service

import com.example.kotlindemoreactive.model.entity.WeatherStation
import com.example.kotlindemoreactive.repository.WeatherStationRepository
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service
import reactor.core.publisher.Sinks
import reactor.util.concurrent.Queues

@Service
class WeatherStationService(private val weatherStationRepository: WeatherStationRepository) {

    private val hotStream =
        Sinks.many().multicast().onBackpressureBuffer<WeatherStation>(Queues.SMALL_BUFFER_SIZE, false)

    suspend fun saveWeatherStation(weatherStation: WeatherStation): WeatherStation =
        weatherStationRepository.save(weatherStation)
            .also { hotStream.tryEmitNext(it).orThrow() }

    suspend fun existWeatherStation(name: String) = weatherStationRepository.existsWeatherStationByName(name)

    fun getAll() = weatherStationRepository.findAll()
    fun subscribe() = hotStream.asFlux().asFlow()
}
