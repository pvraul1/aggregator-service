package com.rperez365.aggregator.client

import com.rperez365.aggregator.dto.CustomerInformation
import com.rperez365.aggregator.dto.StockTradeRequest
import com.rperez365.aggregator.dto.StockTradeResponse
import com.rperez365.aggregator.exception.ApplicationExceptions
import mu.KLogging
import org.springframework.http.ProblemDetail
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException.BadRequest
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound
import reactor.core.publisher.Mono

class CustomerServiceClient(
    private val client: WebClient
) {

    companion object : KLogging()

    fun getCustomerInformation(customerId: String): Mono<CustomerInformation> {
        return client.get()
            .uri("/customers/{customerId}", customerId)
            .retrieve()
            .bodyToMono(CustomerInformation::class.java)
            .onErrorResume { ex ->
                if (ex is NotFound) {
                    ApplicationExceptions.customerNotFound(customerId)
                } else {
                    Mono.error(ex)
                }
            }
    }

    fun trade(customerId: String, request: StockTradeRequest): Mono<StockTradeResponse> {
        return client.post()
            .uri("/customers/{customerId}/trade", customerId)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(StockTradeResponse::class.java)
            .onErrorResume { ex ->
                when (ex) {
                    is NotFound -> {
                        ApplicationExceptions.customerNotFound(customerId)
                    }

                    is BadRequest -> {
                        handleException(ex)
                    }

                    else -> {
                        Mono.error(ex)
                    }
                }
            }
    }

    private fun <T>  handleException(exception: BadRequest): Mono<T> {
        val pd = exception.getResponseBodyAs(ProblemDetail::class.java)
        val message = pd?.detail ?: exception.message

        return ApplicationExceptions.invalidTradeRequest(message)
    }

}