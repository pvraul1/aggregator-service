package com.rperez365.aggregator.dto

import com.rperez365.aggregator.domain.Ticker
import com.rperez365.aggregator.domain.TradeAction

data class StockTradeRequest(

    val ticker: Ticker,
    val price: Int,
    val quantity: Int,
    val action: TradeAction

)
