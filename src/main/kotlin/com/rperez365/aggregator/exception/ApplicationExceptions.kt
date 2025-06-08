package com.rperez365.aggregator.exception

import org.apache.logging.log4j.message.Message
import reactor.core.publisher.Mono

object ApplicationExceptions {

    fun <T> customerNotFound(customerId: String): Mono<T> {
        return Mono.error(CustomerNotFoundException(customerId))
    }

    fun <T> invalidTradeRequest(message: String): Mono<T> {
        return Mono.error(InvalidTradeRequestException(message))
    }

    fun <T> missingTicker(): Mono<T> {
        return Mono.error(InvalidTradeRequestException("Ticker is required"))
    }

    fun <T> missingTradeAction(): Mono<T> {
        return Mono.error(InvalidTradeRequestException("Trade action is required"))
    }

    fun <T> invalidQuantity(): Mono<T> {
        return Mono.error(InvalidTradeRequestException("Quantity should be > 0"))
    }

}