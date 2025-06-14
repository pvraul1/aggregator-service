package com.rperez365.aggregator.config

import com.rperez365.aggregator.client.CustomerServiceClient
import com.rperez365.aggregator.client.StockServiceClient
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ServiceClientConfiguration {

    companion object : KLogging()

    @Bean
    fun customerServiceClient(@Value("\${customer.service.url}") baseUrl: String): CustomerServiceClient {
        return CustomerServiceClient(this.createWebClient(baseUrl))
    }

    @Bean
    fun stockServiceClient(@Value("\${stock.service.url}") baseUrl: String): StockServiceClient {
        return StockServiceClient(this.createWebClient(baseUrl))
    }

    private fun createWebClient(baseUrl: String): WebClient {
        logger.info { "base url: $baseUrl" }

        return WebClient.builder()
            .baseUrl(baseUrl)
            .build()
    }

}