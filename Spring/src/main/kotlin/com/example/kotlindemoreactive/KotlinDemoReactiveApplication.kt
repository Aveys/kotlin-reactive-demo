package com.example.kotlindemoreactive

import com.example.kotlindemoreactive.model.entity.WeatherStation
import com.example.kotlindemoreactive.repository.WeatherStationRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.stereotype.Component
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
@EnableReactiveMongoRepositories
@ConfigurationPropertiesScan("com.example.kotlindemoreactive.config")
class KotlinDemoReactiveApplication

@Component
class AppStartupRunner(private val weatherStationRepository: WeatherStationRepository) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        runBlocking {
            if (weatherStationRepository.count() == 0L) {
                val stationMontreal = WeatherStation(id = "yul", name = "yul", location = "montreal")
                val stationOttawa = WeatherStation(id = "yow", name = "yow", location = "ottawa")
                val stationQuebec = WeatherStation(id = "yqb", name = "yqb", location = "québec")
                val list = listOf(stationMontreal, stationOttawa, stationQuebec)
                weatherStationRepository.saveAll(list).toList()
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<KotlinDemoReactiveApplication>(*args)
}
