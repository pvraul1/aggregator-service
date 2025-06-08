package com.rperez365.aggregator.validator

import com.rperez365.aggregator.dto.TradeRequest
import com.rperez365.aggregator.exception.ApplicationExceptions
import reactor.core.publisher.Mono

object RequestValidator {

    fun validate(): (Mono<TradeRequest>) -> Mono<TradeRequest> {
        return { mono ->
            mono.filter { hasTicker(it) }
                .switchIfEmpty(ApplicationExceptions.missingTicker())
                .filter { hasTradeAction(it) }
                .switchIfEmpty(ApplicationExceptions.missingTradeAction())
                .filter { isValidQuantity(it) }
                .switchIfEmpty(ApplicationExceptions.invalidQuantity())
        }
    }

    private fun hasTicker(dto: TradeRequest): Boolean = dto.ticker != null

    private fun hasTradeAction(dto: TradeRequest): Boolean = dto.action != null

    private fun isValidQuantity(dto: TradeRequest): Boolean = dto.quantity != null && dto.quantity!! > 0
}