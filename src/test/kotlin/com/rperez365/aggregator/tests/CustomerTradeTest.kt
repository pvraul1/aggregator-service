package com.rperez365.aggregator.tests

import com.rperez365.aggregator.domain.Ticker
import com.rperez365.aggregator.domain.TradeAction
import com.rperez365.aggregator.dto.TradeRequest
import mu.KLogging
import org.junit.jupiter.api.Test
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import org.mockserver.model.RegexBody

import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient



class CustomerTradeTest : AbstractIntegrationTest() {

    companion object : KLogging()

    @Test
    fun tradeSuccess() {

        val customerId = "6842f8cb7f09348aa874ee91"

        mockCustomerTrade("customer-service/customer-trade-200.json", 200, customerId)

        val tradeRequest = TradeRequest(Ticker.GOOGLE, TradeAction.BUY, 2)
        postTrade(tradeRequest, HttpStatus.OK, customerId)
            .jsonPath("$.balance").isEqualTo(9780)
            .jsonPath("$.totalPrice").isEqualTo(220)

    }

    @Test
    fun tradeFailure() {

        val customerId = "6842f8cb7f09348aa874ee92"

        mockCustomerTrade("customer-service/customer-trade-400.json", 400, customerId)

        val tradeRequest = TradeRequest(Ticker.GOOGLE, TradeAction.BUY, 2)
        postTrade(tradeRequest, HttpStatus.BAD_REQUEST, customerId)
            .jsonPath("$.detail").isEqualTo("Customer [id=$customerId] does not have enough funds to complete the transaction")

    }

    @Test
    fun inputValidation() {
        val customerId = "6842f8cb7f09348aa874ee92"

        val missingTicker = TradeRequest(null, TradeAction.BUY, 2)
        postTrade(missingTicker, HttpStatus.BAD_REQUEST, customerId)
            .jsonPath("$.detail").isEqualTo("Ticker is required")

        val missingAction = TradeRequest(Ticker.GOOGLE, null, 2)
        postTrade(missingAction, HttpStatus.BAD_REQUEST, customerId)
            .jsonPath("$.detail").isEqualTo("Trade action is required")

        val invalidQuantity = TradeRequest(Ticker.GOOGLE, TradeAction.BUY, -2)
        postTrade(invalidQuantity, HttpStatus.BAD_REQUEST, customerId)
            .jsonPath("$.detail").isEqualTo("Quantity should be > 0")
    }

    private fun mockCustomerTrade(path: String, responseCode: Int, customerId: String) {

        // mock stock-service price response
        val stockResponseBody = this.resourceToString("stock-service/stock-price-200.json")
        mockServerClient
            .`when`(HttpRequest.request("/stock/GOOGLE"))
            .respond(
                HttpResponse.response(stockResponseBody)
                    .withStatusCode(200)
                    .withContentType(MediaType.APPLICATION_JSON)
            )

        val customerResponseBody = this.resourceToString(path)
        mockServerClient
            .`when`(HttpRequest.request("/customers/$customerId/trade")
                .withMethod("POST")
                .withBody(RegexBody.regex(".*\"price\":110.*"))
            )
            .respond(
                HttpResponse.response(customerResponseBody)
                    .withStatusCode(responseCode)
                    .withContentType(MediaType.APPLICATION_JSON)
            )

    }

    private fun postTrade(tradeRequest: TradeRequest, expectedStatus: HttpStatus, customerId: String): WebTestClient.BodyContentSpec {
        return client.post()
            .uri("/customers/{customerId}/trade", customerId)
            .bodyValue(tradeRequest)
            .exchange()
            .expectStatus().isEqualTo(expectedStatus)
            .expectBody()
            .consumeWith { response ->
                val bodyBytes = response.responseBody
                if (bodyBytes != null) {
                    val body = String(bodyBytes)
                    logger("🔍 BODY: $body")
                } else {
                    logger("⚠️ No response body!")
                }
            }
    }

}