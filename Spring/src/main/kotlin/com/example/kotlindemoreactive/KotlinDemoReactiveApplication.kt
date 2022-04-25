package com.example.kotlindemoreactive

import com.example.kotlindemoreactive.model.entity.WeatherStation
import com.example.kotlindemoreactive.repository.WeatherStationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
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
        CoroutineScope(Dispatchers.Default).launch {
            if (weatherStationRepository.count() == 0L) {
                val stationMontreal = WeatherStation(id = "yul", name = "yul", location = "montreal")
                val stationOttawa = WeatherStation(id = "yow", name = "yow", location = "ottawa")
                val stationQuebec = WeatherStation(id = "yqb", name = "yqb", location = "québec")
                val flow = flowOf(stationMontreal, stationOttawa, stationQuebec)
                weatherStationRepository.saveAll(flow)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<KotlinDemoReactiveApplication>(*args)
}
