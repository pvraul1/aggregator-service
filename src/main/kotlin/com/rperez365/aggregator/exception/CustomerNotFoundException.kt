package com.rperez365.aggregator.exception

import java.lang.RuntimeException

class CustomerNotFoundException(id: String) : RuntimeException(
    "Customer [id=$id] is not found"
)
