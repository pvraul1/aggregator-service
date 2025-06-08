package com.rperez365.aggregator.client

import com.rperez365.aggregator.domain.Ticker
import com.rperez365.aggregator.dto.PriceUpdate
import com.rperez365.aggregator.dto.StockPriceResponse
import mu.KLogging
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class StockServiceClient(
    private val client: WebClient
) {

    companion object : KLogging()

    fun getStockPrice(ticker: Ticker): Mono<StockPriceResponse> {
        return client.get()
            .uri("/stock/{ticker}", ticker)
            .retrieve()
            .bodyToMono(StockPriceResponse::class.java)
    }

    fun getPriceUpdates(): Flux<PriceUpdate> {
        return client.get()
            .uri("/stock/price-stream")
            .accept(MediaType.APPLICATION_NDJSON)
            .retrieve()
            .bodyToFlux(PriceUpdate::class.java)
    }

}