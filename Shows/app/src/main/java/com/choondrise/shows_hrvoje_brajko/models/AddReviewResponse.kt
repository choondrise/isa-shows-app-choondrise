package com.choondrise.shows_hrvoje_brajko.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddReviewResponse(
    @SerialName("review") val review: Review
)

@Serializable
data class Review(
    @SerialName("id") val id: String,
    @SerialName("comment") val comment: String,
    @SerialName("rating") val rating: Int,
    @SerialName("show_id") val showId: String,
    @SerialName("user") val user: User
)
