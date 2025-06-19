package com.rperez365.aggregator.controller

import com.rperez365.aggregator.dto.CustomerInformation
import com.rperez365.aggregator.dto.StockTradeResponse
import com.rperez365.aggregator.dto.TradeRequest
import com.rperez365.aggregator.service.CustomerPortfolioService
import com.rperez365.aggregator.validator.RequestValidator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("customers")
class CustomerPortfolioController(
    private val customerPortfolioService: CustomerPortfolioService
) {

    @GetMapping("/{customerId}")
    fun getCusotmerInformation(@PathVariable customerId: String): Mono<CustomerInformation> {
        return customerPortfolioService.getCustomerInformation(customerId)
    }

    @PostMapping("/{customerId}/trade")
    fun trade(@PathVariable customerId: String, @RequestBody mono: Mono<TradeRequest>): Mono<StockTradeResponse> {
        return mono.transform(RequestValidator.validate())
            .flatMap { req -> customerPortfolioService.trade(customerId, req) }
    }

}