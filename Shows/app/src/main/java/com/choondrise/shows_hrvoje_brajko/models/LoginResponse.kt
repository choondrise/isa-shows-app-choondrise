package com.choondrise.shows_hrvoje_brajko.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse (
    @SerialName("user") val user: User
        )