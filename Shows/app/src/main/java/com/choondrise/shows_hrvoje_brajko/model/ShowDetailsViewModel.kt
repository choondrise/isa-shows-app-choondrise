package com.choondrise.shows_hrvoje_brajko.model

import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentShowDetailsBinding
import com.choondrise.shows_hrvoje_brajko.models.DisplayShowResponse
import com.choondrise.shows_hrvoje_brajko.models.Review
import com.choondrise.shows_hrvoje_brajko.models.Show
import com.choondrise.shows_hrvoje_brajko.networking.ApiModule
import com.choondrise.shows_hrvoje_brajko.ui.ReviewsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel : ViewModel() {

    private val reviews = mutableListOf<Review>()

    private val reviewsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }

    private val showLiveData: MutableLiveData<Show> by lazy {
        MutableLiveData<Show>()
    }

    fun getReviewsLiveData(): LiveData<List<Review>> {
        return reviewsLiveData
    }

    fun getShowLiveData(): LiveData<Show> {
        return showLiveData
    }

    private fun updateReviewsLiveData() {
        reviewsLiveData.value = reviews
    }

    fun displayShow(id: String) {
        ApiModule.retrofit.displayShow(id).enqueue(object: Callback<DisplayShowResponse> {
            override fun onResponse(
                call: Call<DisplayShowResponse>,
                response: Response<DisplayShowResponse>
            ) {
                showLiveData.value = response.body()?.show
            }

            override fun onFailure(call: Call<DisplayShowResponse>, t: Throwable) {

            }

        })
    }

    fun addReview(review: Review) {
        reviews.add(review)
        updateReviewsLiveData()
    }

    fun updateRating(binding: FragmentShowDetailsBinding, totalRating: Int, adapter: ReviewsAdapter?) {
        val rating: Float = (totalRating * 1.0f / adapter!!.itemCount)
        binding.ratingTotal.isVisible = true
        binding.ratingBar.isVisible = true
        binding.ratingBar.setIsIndicator(true)
        binding.reviewsEmptyText.isVisible = false
        binding.ratingTotal.text = (adapter!!.itemCount.toString() + " REVIEWS, " + rating.toString() + " AVERAGE")
        binding.ratingBar.rating = rating
    }

}