package com.example.kotlindemoreactive.model.dto

data class WeatherStationUpdates(val status: WeatherStationUpdatesStatus, val content: WeatherStationDTO)

enum class WeatherStationUpdatesStatus {
    CREATED,
    UPDATED,
    DELETED
}
