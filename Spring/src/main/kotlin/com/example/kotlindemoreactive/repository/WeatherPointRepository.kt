package com.example.kotlindemoreactive.repository

import com.example.kotlindemoreactive.model.entity.WeatherPoint
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface WeatherPointRepository : ReactiveMongoRepository<WeatherPoint, String>
