package com.example.kotlindemoreactive.repository

import com.example.kotlindemoreactive.model.entity.WeatherStation
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface WeatherStationRepository : ReactiveMongoRepository<WeatherStation, String> {
    fun existsWeatherStationByName(name: String): Mono<Boolean>
}
