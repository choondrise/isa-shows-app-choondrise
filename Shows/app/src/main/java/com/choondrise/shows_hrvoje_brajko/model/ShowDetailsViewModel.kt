package com.choondrise.shows_hrvoje_brajko.model

import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentShowDetailsBinding
import com.choondrise.shows_hrvoje_brajko.models.*
import com.choondrise.shows_hrvoje_brajko.networking.ApiModule
import com.choondrise.shows_hrvoje_brajko.ui.ReviewsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel : ViewModel() {

    private val reviewsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }

    private val reviewLiveData: MutableLiveData<Review> by lazy {
        MutableLiveData<Review>()
    }

    private val showLiveData: MutableLiveData<Show> by lazy {
        MutableLiveData<Show>()
    }

    fun getReviewLiveData(): LiveData<Review> {
        return reviewLiveData
    }

    fun getReviewsLiveData(): LiveData<List<Review>> {
        return reviewsLiveData
    }

    fun getShowLiveData(): LiveData<Show> {
        return showLiveData
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

    fun addReview(rating: Int, comment: String, showId: Int) {
        ApiModule.retrofit.addReview(AddReviewRequest(rating, comment, showId)).enqueue(object: Callback<AddReviewResponse> {
            override fun onResponse(
                call: Call<AddReviewResponse>,
                response: Response<AddReviewResponse>
            ) {
                reviewLiveData.value = response.body()?.review
                displayShow(showId.toString())
                listReviews(showId)
            }

            override fun onFailure(call: Call<AddReviewResponse>, t: Throwable) {
                t.localizedMessage
            }

        })
    }

    fun listReviews(id: Int) {
        ApiModule.retrofit.listReviews(id).enqueue(object: Callback<ListReviewsResponse> {
            override fun onResponse(
                call: Call<ListReviewsResponse>,
                response: Response<ListReviewsResponse>
            ) {
                reviewsLiveData.value = response.body()?.reviews
            }

            override fun onFailure(call: Call<ListReviewsResponse>, t: Throwable) {
            }

        })
    }

}