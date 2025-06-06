package com.rperez365.aggregator.dto

import com.rperez365.aggregator.domain.Ticker

data class StockPriceResponse(
    val ticker: Ticker,
    val price: Int
)
