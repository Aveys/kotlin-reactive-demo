package com.example.kotlindemoreactive.controller

import com.example.kotlindemoreactive.model.dto.WeatherPointDTO
import com.example.kotlindemoreactive.model.entity.WeatherPoint
import com.example.kotlindemoreactive.service.WeatherPointService
import com.example.kotlindemoreactive.service.WeatherStationService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/stations")
class WeatherStationController(private val weatherStationService: WeatherStationService, private val weatherPointService: WeatherPointService) {

    @PostMapping("/{stationId}/points")
    fun addPoint(@PathVariable stationId: String, @RequestBody point: WeatherPointDTO): Mono<WeatherPoint> {
        return weatherPointService.saveWeatherPoint(stationId, point)
    }

    @GetMapping("/{stationId}/points", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun getPointofStation(@PathVariable stationId: String): Flux<WeatherPointDTO> {
        return weatherPointService.listByStationId(stationId).map { it.toDTO() }
    }
}
