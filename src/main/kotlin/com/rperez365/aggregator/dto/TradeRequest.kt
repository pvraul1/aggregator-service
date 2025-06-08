package com.rperez365.aggregator.dto

import com.rperez365.aggregator.domain.Ticker
import com.rperez365.aggregator.domain.TradeAction

data class TradeRequest(
    val ticker: Ticker?,
    val action: TradeAction?,
    val quantity: Int?
)
