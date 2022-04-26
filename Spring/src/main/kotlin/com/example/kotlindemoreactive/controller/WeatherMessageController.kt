package com.example.kotlindemoreactive.controller

import com.example.kotlindemoreactive.model.dto.WeatherPointDTO
import com.example.kotlindemoreactive.model.entity.WeatherStation
import com.example.kotlindemoreactive.service.WeatherPointService
import com.example.kotlindemoreactive.service.WeatherStationService
import kotlinx.coroutines.flow.map
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.ServerRequest

@Controller
class WeatherMessageController(
    private val weatherStationService: WeatherStationService,
    private val weatherPointService: WeatherPointService
) {

    @MessageMapping("point.get.all")
    suspend fun listAllPoints() = weatherPointService.listAll()

    @MessageMapping("point.add.{stationId}")
    suspend fun addPoint(@DestinationVariable stationId: String, @Payload point: WeatherPointDTO) {
        weatherPointService.saveWeatherPoint(stationId, point)
    }

    @MessageMapping("point.get.station.{stationId}")
    suspend fun getPointOfStation(@DestinationVariable stationId: String) =
        weatherPointService.listByStationId(stationId).map { it.toDTO() }

    @MessageMapping("station.get.all")
    suspend fun getAllStations(serverRequest: ServerRequest) = weatherStationService.getAll()

    @MessageMapping("station.get.subscribe")
    suspend fun getSubscribe() = weatherStationService.subscribe()

    @MessageMapping("station.add")
    suspend fun addStation(@Payload station: WeatherStation) {
        weatherStationService.saveWeatherStation(station)
    }
}
