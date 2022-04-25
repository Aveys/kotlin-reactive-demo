package com.example.kotlindemoreactive.repository

import com.example.kotlindemoreactive.model.entity.WeatherPoint
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WeatherPointRepository : CoroutineCrudRepository<WeatherPoint, String>
