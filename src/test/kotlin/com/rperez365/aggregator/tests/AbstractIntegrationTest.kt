package com.rperez365.aggregator.tests

import org.junit.jupiter.api.BeforeAll
import org.mockserver.client.MockServerClient
import org.mockserver.configuration.ConfigurationProperties
import org.mockserver.springtest.MockServerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import java.nio.file.Path

@SpringBootTest(
    properties = [
        "customer.service.url=http://localhost:\${mockServerPort}",
        "stock.service.url=http://localhost:\${mockServerPort}"
    ]
)
@AutoConfigureWebTestClient
@MockServerTest
abstract class AbstractIntegrationTest {

    companion object {
        private val TEST_RESOURCES_PATH: Path = Path.of("src/test/resources")

        @JvmStatic
        @BeforeAll
        fun setup() {
            ConfigurationProperties.disableLogging(true)
        }
    }

    // it is set by @MockServerTest
    protected lateinit var mockServerClient: MockServerClient

    @Autowired
    protected lateinit var client: WebTestClient

}