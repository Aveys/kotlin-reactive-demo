package com.example.kotlindemoreactive.handler

import com.example.kotlindemoreactive.model.dto.WeatherPointDTO
import com.example.kotlindemoreactive.service.WeatherPointService
import com.example.kotlindemoreactive.service.WeatherStationService
import kotlinx.coroutines.flow.map
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBodyOrNull
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import java.net.URI

@Component
class WeatherStationHandler(private val weatherPointService: WeatherPointService, private val weatherStationService: WeatherStationService) {

    suspend fun addPoint(serverRequest: ServerRequest): ServerResponse {
        val stationId = serverRequest.pathVariable("stationId")
        val point = serverRequest.awaitBodyOrNull<WeatherPointDTO>() ?: return ServerResponse.badRequest().buildAndAwait()
        val savedPoint = weatherPointService.saveWeatherPoint(stationId, point)
        return ServerResponse.created(URI.create("/stations/$stationId/points/${savedPoint.id}")).contentType(MediaType.APPLICATION_NDJSON).bodyValueAndAwait(savedPoint)
    }
    suspend fun getPointOfStation(serverRequest: ServerRequest): ServerResponse {
        val stationId = serverRequest.pathVariable("stationId")
        return ServerResponse.ok().bodyAndAwait(weatherPointService.listByStationId(stationId).map { it.toDTO() })
    }

    suspend fun getAllStations(): ServerResponse {
        return ServerResponse.ok().bodyAndAwait(weatherStationService.listAllWeatherStations().map { it.toDto() })
    }


    suspend fun subscribeToStationUpdates(serverRequest: ServerRequest): ServerResponse {
        return ServerResponse.ok().bodyAndAwait(weatherStationService.subscribeToStationUpdates()
    }
}
