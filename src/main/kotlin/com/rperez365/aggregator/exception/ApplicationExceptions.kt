package com.rperez365.aggregator.exception

import reactor.core.publisher.Mono

object ApplicationExceptions {

    fun <T> customerNotFound(customerId: String): Mono<T> {
        return Mono.error(CustomerNotFoundException(customerId))
    }

}