package com.choondrise.shows_hrvoje_brajko.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DisplayShowResponse(
    @SerialName("show") val show: Show
)