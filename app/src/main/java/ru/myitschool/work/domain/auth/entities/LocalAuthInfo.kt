package ru.myitschool.work.domain.auth.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocalAuthInfo(
    @SerialName("basicToken")
    val basicToken: String
)
