package com.rperez365.aggregator.dto

data class CustomerInformation(

    val id: String?,
    val name: String,
    val balance: Int,
    val holdings: List<Holding>?


)
