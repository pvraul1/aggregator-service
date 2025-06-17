package com.rperez365.aggregator.tests

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.mockserver.client.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import java.nio.file.Files
import kotlin.io.path.Path

@SpringBootTest
@AutoConfigureWebTestClient
abstract class AbstractIntegrationTest {

    companion object {

        private val TEST_RESOURCES_PATH = Path("src/test/resources")

        protected lateinit var mockServer: ClientAndServer

        @JvmStatic
        protected lateinit var mockServerClient: MockServerClient

        protected var port: Int = 0

        @JvmStatic
        @BeforeAll
        fun startMockServer() {
            mockServer = ClientAndServer.startClientAndServer(0)
            port = mockServer.localPort
            mockServerClient = MockServerClient("localhost", port)

            
        }

        @JvmStatic
        @AfterAll
        fun stopMockServer() {
            mockServer.stop()
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.add("customer.service.url") { "http://localhost:$port" }
            registry.add("stock.service.url") { "http://localhost:$port" }
        }
    }

    @Autowired
    protected lateinit var client: WebTestClient

    fun resourceToString(realativePath: String): String {
        return Files.readString(TEST_RESOURCES_PATH.resolve(realativePath))
    }

}
