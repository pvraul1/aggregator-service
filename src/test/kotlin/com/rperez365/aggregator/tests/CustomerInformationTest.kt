package com.rperez365.aggregator.tests

import mu.KLogging
import org.junit.jupiter.api.Test
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient

class CustomerInformationTest : AbstractIntegrationTest() {

    companion object : KLogging()

    @Test
    fun customerInformation() {

        val customerId = "6842f8cb7f09348aa874ee91"

        // mock customer service
        mockCustomerInformation("customer-service/customer-information-200.json", 200, customerId)

        getCustomerInformation(customerId, HttpStatus.OK)
            .jsonPath("$.id").isEqualTo(customerId)
            .jsonPath("$.name").isEqualTo("Sam")
            .jsonPath("$.balance").isEqualTo(10000)
            .jsonPath("$.holdings").isNotEmpty

    }

    @Test
    fun customerNotFound() {

        val customerId = "6842f8cb7f09348aa874ee92"

        // mock customer service
        mockCustomerInformation("customer-service/customer-information-404.json", 404, customerId)

        getCustomerInformation(customerId, HttpStatus.NOT_FOUND)
            .jsonPath("$.detail").isEqualTo("Customer [id=$customerId] is not found")
            .jsonPath("$.title").isNotEmpty

    }

    private fun mockCustomerInformation(path: String, responseCode: Int, customerId: String) {
        // mock customer service
        val responseBody = super.resourceToString(path)

        mockServerClient
            .`when`(HttpRequest.request("/customers/$customerId"))
            .respond(
                HttpResponse.response(responseBody)
                    .withStatusCode(responseCode)
                    .withContentType(MediaType.APPLICATION_JSON)
            )
    }

    private fun getCustomerInformation(customerId: String, expectedStatus: HttpStatus): WebTestClient.BodyContentSpec {
        return client.get()
            .uri("/customers/{customerId}", customerId)
            .exchange()
            .expectStatus().isEqualTo(expectedStatus)
            .expectBody()
            .consumeWith { response ->
                val bodyBytes = response.responseBody
                if (bodyBytes != null) {
                    val body = String(bodyBytes)
                    logger("🔍 BODY: $body") // o logger.info("...")
                } else {
                    logger("⚠️ No response body!")
                }
            }
    }

}