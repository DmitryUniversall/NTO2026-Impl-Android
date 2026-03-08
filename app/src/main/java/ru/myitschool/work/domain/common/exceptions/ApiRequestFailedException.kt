package ru.myitschool.work.domain.common.exceptions

import io.ktor.client.statement.HttpResponse

class ApiRequestFailedException(
    message: String,
    val response: HttpResponse
) : RuntimeException(message)
