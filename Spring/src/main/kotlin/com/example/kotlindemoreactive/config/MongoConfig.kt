package com.example.kotlindemoreactive.config

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class MongoConfig : AbstractReactiveMongoConfiguration() {
    override fun getDatabaseName() = "weathernetwork"

    @Bean
    fun mongoClient(): MongoClient = MongoClients.create("mongodb://localhost:27017/weathernetwork")

    @Bean
    fun rsocketStrategies() = RSocketStrategies.builder().encoders {
        it.add(Jackson2JsonEncoder())
    }.decoders {
        it.add(Jackson2JsonDecoder())
    }.build()

    @Bean
    fun messageHandler(rSocketStrategies: RSocketStrategies) = RSocketMessageHandler().apply {
        setRSocketStrategies(rSocketStrategies)
    }

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
