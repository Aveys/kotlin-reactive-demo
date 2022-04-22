package com.example.kotlindemoreactive.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class WeatherStation(
    @Id
    val id: String? = null,
    val name: String,
    val location: String,
)
