package com.choondrise.shows_hrvoje_brajko.model

import androidx.annotation.DrawableRes

data class Show(
    val ID: Int,
    val name: String,
    val description: String,
    @DrawableRes val imageResourceId: Int
)