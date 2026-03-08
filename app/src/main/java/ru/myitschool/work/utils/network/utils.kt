package ru.myitschool.work.utils.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import ru.myitschool.work.domain.auth.exceptions.UnauthenticatedException
import ru.myitschool.work.domain.common.exceptions.ApiRequestFailedException

suspend inline fun <reified T> HttpResponse.getOrError(): T {
    if (this.status.isSuccess()) return body<T>()

    when (this.status) {
        HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden -> throw UnauthenticatedException("Unauthenticated: ${bodyAsText()}")
        else -> throw ApiRequestFailedException("Api request failed: ${bodyAsText()}", this)
    }
}
