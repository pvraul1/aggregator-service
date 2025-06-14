package com.rperez365.aggregator.advice

import com.rperez365.aggregator.exception.CustomerNotFoundException
import com.rperez365.aggregator.exception.InvalidTradeRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.net.URI

@ControllerAdvice
class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException::class)
    fun handleException(ex: CustomerNotFoundException): ProblemDetail {
        return build(HttpStatus.NOT_FOUND, ex) { problem ->
            problem.type = URI.create("http://example.com/problems/customer-not-found")
            problem.title = "Customer Not Found"
        }
    }

    @ExceptionHandler(InvalidTradeRequestException::class)
    fun handleException(ex: InvalidTradeRequestException): ProblemDetail {
        return build(HttpStatus.BAD_REQUEST, ex) { problem ->
            problem.type = URI.create("http://example.com/problems/invalid-trade-request")
            problem.title = "Invalid Trade Request"
        }
    }

    private fun build(status: HttpStatus, ex: Exception, consumer: (ProblemDetail) -> Unit): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(status, ex.message)
        consumer(problem)

        return problem
    }

}