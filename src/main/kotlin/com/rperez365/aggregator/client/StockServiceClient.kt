package com.rperez365.aggregator.client

import com.rperez365.aggregator.domain.Ticker
import com.rperez365.aggregator.dto.PriceUpdate
import com.rperez365.aggregator.dto.StockPriceResponse
import mu.KLogging
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration
import java.util.*

class StockServiceClient(
    private val client: WebClient
) {

    companion object : KLogging()

    private var flux: Flux<PriceUpdate>? = null

    fun getStockPrice(ticker: Ticker): Mono<StockPriceResponse> {
        return client.get()
            .uri("/stock/{ticker}", ticker)
            .retrieve()
            .bodyToMono(StockPriceResponse::class.java)
    }

    fun priceUpdatesStream(): Flux<PriceUpdate>? {
        if (Objects.isNull(this.flux)) {
            this.flux = this.getPriceUpdates()
        }

        return this.flux
    }

    private fun getPriceUpdates(): Flux<PriceUpdate> {
        return client.get()
            .uri("/stock/price-stream")
            .accept(MediaType.APPLICATION_NDJSON)
            .retrieve()
            .bodyToFlux(PriceUpdate::class.java)
            .retryWhen(retry())
            .cache(1)
    }

    private fun retry(): Retry {
        return Retry.fixedDelay(100, Duration.ofSeconds(1))
            .doBeforeRetry { retryRetrySignal -> logger.error("Stock service price steam call failed. Retrying: ${retryRetrySignal.failure().message}") }
    }

}
