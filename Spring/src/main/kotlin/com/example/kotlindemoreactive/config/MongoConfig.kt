package com.example.kotlindemoreactive.config

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration

@Configuration
class MongoConfig : AbstractReactiveMongoConfiguration() {
    override fun getDatabaseName() = "weathernetwork"

    @Bean
    fun mongoClient(): MongoClient = MongoClients.create("mongodb://localhost:27017/weathernetwork")
}
