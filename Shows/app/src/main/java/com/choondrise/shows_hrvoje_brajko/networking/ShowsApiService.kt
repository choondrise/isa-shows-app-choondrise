package com.choondrise.shows_hrvoje_brajko.networking

import com.choondrise.shows_hrvoje_brajko.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/reviews")
    fun addReview(@Body request: AddReviewRequest): Call<AddReviewResponse>

    @GET("/shows/{show_id}/reviews")
    fun listReviews(@Body showId: Int): Call<ListReviewsResponse>

    @GET("/shows")
    fun listShows(): Call<ListShowsResponse>

    @GET("/shows/{id}")
    fun displayShow(@Body showId: Int): Call<DisplayShowResponse>
}