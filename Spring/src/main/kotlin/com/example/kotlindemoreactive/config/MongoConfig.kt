package com.example.kotlindemoreactive.config

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class MongoConfig : AbstractReactiveMongoConfiguration() {
    override fun getDatabaseName() = "weathernetwork"

    @Bean
    fun mongoClient(): MongoClient = MongoClients.create("mongodb://localhost:27017/weathernetwork")

    @Bean
    fun corsWebFilter(): CorsWebFilter? {
        val corsConfig = CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            maxAge = 8000L
            allowedHeaders = listOf("PUT", "GET", "POST")
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        return CorsWebFilter(source)
    }
}
