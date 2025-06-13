package com.rperez365.aggregator.service

import com.rperez365.aggregator.client.CustomerServiceClient
import com.rperez365.aggregator.client.StockServiceClient
import com.rperez365.aggregator.dto.CustomerInformation
import com.rperez365.aggregator.dto.StockPriceResponse
import com.rperez365.aggregator.dto.StockTradeRequest
import com.rperez365.aggregator.dto.StockTradeResponse
import com.rperez365.aggregator.dto.TradeRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CustomerPortfolioService(
    private val stockServiceClient: StockServiceClient,
    private val customerServiceClient: CustomerServiceClient
) {

    fun getCustomerInformation(customerId: String): Mono<CustomerInformation> {
        return customerServiceClient.getCustomerInformation(customerId)
    }

    fun trade(customerId: String, request: TradeRequest): Mono<StockTradeResponse> {
        return stockServiceClient.getStockPrice(request.ticker!!)
            .map(StockPriceResponse::price)
            .map { price ->  toStockTradeRequest(request, price)}
            .flatMap { req -> customerServiceClient.trade(customerId, req) }
    }

    private fun toStockTradeRequest(request: TradeRequest, price: Int): StockTradeRequest {
        return StockTradeRequest(
            request.ticker,
            price,
            request.quantity,
            request.action
        )
    }

}