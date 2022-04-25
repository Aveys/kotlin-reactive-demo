package com.example.kotlindemoreactive.model.exceptions

data class WeatherStationNotFound(val stationId: String) : RuntimeException("Weather station with id $stationId not found")
