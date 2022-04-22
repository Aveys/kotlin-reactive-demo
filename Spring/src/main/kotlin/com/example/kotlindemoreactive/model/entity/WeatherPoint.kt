package com.example.kotlindemoreactive.model.entity

import com.example.kotlindemoreactive.model.dto.WeatherPointDTO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
data class WeatherPoint(
    @Id
    val id: String? = null,
    val time: Instant = Instant.now(),
    val temp: Double,
    val unit: String,
    val wind: String,
    val stationId: String
) {
    fun toDTO() = WeatherPointDTO(
        id = id,
        time = time.toEpochMilli(),
        temp = temp,
        unit = unit,
        wind = wind,
        stationId = stationId
    )
}
