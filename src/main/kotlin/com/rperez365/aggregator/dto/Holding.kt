package com.rperez365.aggregator.dto

import com.rperez365.aggregator.domain.Ticker

data class Holding(

    val ticker: Ticker,
    val quantity: Int

)
