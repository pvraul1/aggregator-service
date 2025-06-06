package com.rperez365.aggregator.dto

import com.rperez365.aggregator.domain.Ticker
import com.rperez365.aggregator.domain.TradeAction

data class StockTradeResponse(

    val customerId: String?,
    val ticker: Ticker,
    val price: Int,
    val quantity: Int,
    val action: TradeAction,
    val totalPrice: Int,
    val balance: Int

)
