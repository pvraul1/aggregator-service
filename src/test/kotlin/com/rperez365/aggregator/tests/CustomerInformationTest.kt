package com.rperez365.aggregator.tests

import org.junit.jupiter.api.Test
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient

class CustomerInformationTest : AbstractIntegrationTest() {

    @Test
    fun customerInformation() {

        // mock customer service
        mockCustomerInformation("customer-service/customer-information-200.json", 200)
        super.resourceToString("customer-service/customer-information-200.json")

        getCustomerInformation(HttpStatus.OK)
            .jsonPath("$.id").isEqualTo("6842f8cb7f09348aa874ee91")
            .jsonPath("$.name").isEqualTo("Sam")
            .jsonPath("$.balance").isEqualTo(10000)
            .jsonPath("$.holdings").isNotEmpty

    }

    @Test
    fun customerNotFound() {

        // mock customer service
        mockCustomerInformation("customer-service/customer-information-404.json", 404)
        super.resourceToString("customer-service/customer-information-200.json")

        getCustomerInformation(HttpStatus.NOT_FOUND)
            .jsonPath("$.detail").isEqualTo("Customer [id=6842f8cb7f09348aa874ee91] is not found")
            .jsonPath("$.title").isNotEmpty

    }

    private fun mockCustomerInformation(path: String, responseCode: Int) {
        // mock customer service
        val responseBody = super.resourceToString(path)

        mockServerClient
            .`when`(HttpRequest.request("/customers/6842f8cb7f09348aa874ee91"))
            .respond(
                HttpResponse.response(responseBody)
                    .withStatusCode(responseCode)
                    .withContentType(MediaType.APPLICATION_JSON)
            )
    }

    private fun getCustomerInformation(expectedStatus: HttpStatus): WebTestClient.BodyContentSpec {
        return client.get()
            .uri("/customers/6842f8cb7f09348aa874ee91")
            .exchange()
            .expectStatus().isEqualTo(expectedStatus)
            .expectBody()
            .consumeWith { response ->
                val bodyBytes = response.responseBody
                if (bodyBytes != null) {
                    val body = String(bodyBytes)
                    println("🔍 BODY: $body") // o logger.info("...")
                } else {
                    println("⚠️ No response body!")
                }
            }
    }

}