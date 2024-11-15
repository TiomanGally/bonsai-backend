package de.gally.bonsai.port.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleBonsaiNotFoundException(ex: NotFoundException): Map<String, String> {
        return mapOf("error" to ex.message.orEmpty())
    }

    @ExceptionHandler(UnsupportedFileTypeException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUnsupportedFileTypeException(ex: BonsaiNotFoundException): Map<String, String> {
        return mapOf("error" to ex.message.orEmpty())
    }
}
