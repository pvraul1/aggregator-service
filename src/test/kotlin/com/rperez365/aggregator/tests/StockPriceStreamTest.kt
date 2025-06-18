package com.rperez365.aggregator.tests

import com.rperez365.aggregator.dto.PriceUpdate
import mu.KLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import reactor.test.StepVerifier
import java.util.function.Consumer


class StockPriceStreamTest : AbstractIntegrationTest() {

    companion object : KLogging()

    @Test
    fun priceStream() {

        // mock stock-service streaming response
        val responseBody = this.resourceToString("stock-service/stock-price-stream-200.jsonl")
        mockServerClient
            .`when`(HttpRequest.request("/stock/price-stream"))
            .respond(
                HttpResponse.response(responseBody)
                    .withStatusCode(200)
                    .withContentType(MediaType.parse("application/x-ndjson"))
            )

        // we should get the streaming response via aggregator-service
        this.client.get()
            .uri("/stock/price-stream")
            .accept(org.springframework.http.MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .returnResult<PriceUpdate?>(PriceUpdate::class.java)
            .getResponseBody()
            .doOnNext { price -> logger.info("$price") }
            .`as`(StepVerifier::create)
            .assertNext(Consumer { p: PriceUpdate? -> Assertions.assertEquals(53, p?.price) })
            .assertNext(Consumer { p: PriceUpdate? -> Assertions.assertEquals(54, p?.price) })
            .assertNext(Consumer { p: PriceUpdate? -> Assertions.assertEquals(55, p?.price) })
            .expectComplete()
            .verify()
    }

}