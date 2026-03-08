package ru.myitschool.work.core

import kotlinx.serialization.json.Json

val jsonCore = Json {
    isLenient = true
    ignoreUnknownKeys = true
    explicitNulls = true
    encodeDefaults = true
}
