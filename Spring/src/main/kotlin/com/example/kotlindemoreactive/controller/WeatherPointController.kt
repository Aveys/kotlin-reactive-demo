package com.example.kotlindemoreactive.controller

import com.example.kotlindemoreactive.model.dto.WeatherPointDTO
import com.example.kotlindemoreactive.service.WeatherPointService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/points")
class WeatherPointController(private val weatherPointService: WeatherPointService) {

    @GetMapping(produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun listAllPoints(): Flux<WeatherPointDTO> {
        return weatherPointService.listAll()
    }
}
