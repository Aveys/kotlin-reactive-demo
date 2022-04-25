package com.example.kotlindemoreactive.handler

import com.example.kotlindemoreactive.service.WeatherPointService
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait

@Component
class WeatherPointHandler(private val weatherPointService: WeatherPointService) {

    suspend fun listAllPoints(serverRequest: ServerRequest): ServerResponse =
        ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).bodyAndAwait(weatherPointService.listAll())
}
