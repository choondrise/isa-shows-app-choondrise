package com.choondrise.shows_hrvoje_brajko.networking

import com.choondrise.shows_hrvoje_brajko.models.RegisterRequest
import com.choondrise.shows_hrvoje_brajko.models.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}