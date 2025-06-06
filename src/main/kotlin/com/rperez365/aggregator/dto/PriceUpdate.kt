package com.rperez365.aggregator.dto

import com.rperez365.aggregator.domain.Ticker
import java.time.LocalDateTime

data class PriceUpdate(
    val ticker: Ticker,
    val price: Int,
    val time: LocalDateTime
)
