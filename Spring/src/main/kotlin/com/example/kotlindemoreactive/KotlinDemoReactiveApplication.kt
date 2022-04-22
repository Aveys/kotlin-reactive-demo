package com.example.kotlindemoreactive

import com.example.kotlindemoreactive.model.entity.WeatherStation
import com.example.kotlindemoreactive.repository.WeatherStationRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.stereotype.Component
import org.springframework.web.reactive.config.EnableWebFlux
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

@SpringBootApplication
@EnableWebFlux
@EnableReactiveMongoRepositories
@ConfigurationPropertiesScan("com.example.kotlindemoreactive.config")
class KotlinDemoReactiveApplication

@Component
class AppStartupRunner(private val weatherStationRepository: WeatherStationRepository) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        weatherStationRepository.count()
            .filter { it == 0L }
            .map {
                val stationMontreal = WeatherStation(id = "yul", name = "yul", location = "montreal")
                val stationOttawa = WeatherStation(id = "yow", name = "yow", location = "ottawa")
                val stationQuebec = WeatherStation(id = "yqb", name = "yqb", location = "québec")
                return@map listOf(stationMontreal, stationOttawa, stationQuebec)
            }
            .flatMapMany { Flux.fromIterable(it) }
            .publishOn(Schedulers.boundedElastic())
            .map {
                weatherStationRepository.save(it).subscribe { ws ->
                    println("Saved weather station: $ws")
                }
            }.subscribe()
    }
}

fun main(args: Array<String>) {
    runApplication<KotlinDemoReactiveApplication>(*args)
}
