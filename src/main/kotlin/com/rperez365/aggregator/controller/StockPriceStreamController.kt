package com.rperez365.aggregator.controller

import com.rperez365.aggregator.client.StockServiceClient
import com.rperez365.aggregator.dto.PriceUpdate
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("stock")
class StockPriceStreamController(
    private val stockServiceClient: StockServiceClient
) {

    @GetMapping("/price-stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun priceUpdateStream() : Flux<PriceUpdate> = stockServiceClient.priceUpdatesStream()!!

}