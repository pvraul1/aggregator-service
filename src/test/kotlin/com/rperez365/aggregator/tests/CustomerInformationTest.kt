package com.rperez365.aggregator.tests

import org.junit.jupiter.api.Test
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType

class CustomerInformationTest : AbstractIntegrationTest() {

    @Test
    fun customerInformation() {

        // mock customer service
        val responseBody = super.resourceToString("customer-service/customer-information-200.json")

        mockServerClient
            .`when`(HttpRequest.request("/customers/6842f8cb7f09348aa874ee91"))
            .respond(
                HttpResponse.response(responseBody)
                    .withStatusCode(200)
                    .withContentType(MediaType.APPLICATION_JSON)
            )

        client.get()
            .uri("/customers/6842f8cb7f09348aa874ee91")
            .exchange()
            .expectStatus().isOk
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