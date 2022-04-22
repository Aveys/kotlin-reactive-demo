package com.example.kotlindemoreactive.model.dto

import com.example.kotlindemoreactive.model.entity.WeatherPoint
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.MissingFormatArgumentException

@JsonInclude(JsonInclude.Include.NON_NULL)
data class WeatherPointDTO(
    val id: String? = null,
    val time: Long? = null,
    val temp: Double,
    val unit: String,
    val wind: String,
    var stationId: String? = null
) {
    fun toWeatherPoint() = WeatherPoint(id, temp = temp, unit = unit, wind = wind, stationId = stationId ?: throw MissingFormatArgumentException("stationId"))
}
