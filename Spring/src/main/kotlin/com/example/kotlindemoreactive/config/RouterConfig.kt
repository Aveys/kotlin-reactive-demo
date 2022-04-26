package com.example.kotlindemoreactive.config

import com.example.kotlindemoreactive.handler.WeatherPointHandler
import com.example.kotlindemoreactive.handler.WeatherStationHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfig {
    @Bean
    fun routerFunction(weatherPointHandler: WeatherPointHandler, weatherStationHandler: WeatherStationHandler) =
        coRouter {
            "/stations".nest {
                GET("", weatherStationHandler::getAllStations)
                GET("/subscribe", weatherStationHandler::getSubscribe)
                POST("", weatherStationHandler::addStation)
                POST("/{stationId}/points", weatherStationHandler::addPoint)
                GET("/{stationId}/points", weatherStationHandler::getPointOfStation)
            }
            "/points".nest {
                GET("", weatherPointHandler::listAllPoints)
            }
        }
}
