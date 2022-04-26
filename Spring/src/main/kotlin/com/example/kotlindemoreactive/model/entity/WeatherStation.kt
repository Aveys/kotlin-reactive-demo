package com.example.kotlindemoreactive.model.entity

import com.example.kotlindemoreactive.model.dto.WeatherStationDTO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class WeatherStation(
    @Id
    val id: String? = null,
    val name: String,
    val location: String,
) {
    fun toDto() = WeatherStationDTO(
        id = id,
        name = name,
        location = location,
    )
}
