package com.choondrise.shows_hrvoje_brajko.model

import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentShowDetailsBinding
import com.choondrise.shows_hrvoje_brajko.ui.ReviewsAdapter

class ShowDetailsViewModel : ViewModel() {

    private val reviews = mutableListOf<Review>()
    private var show: Show? = null

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

    fun initShowLiveData(showOther: Show) {
        show = showOther
        showLiveData.value = show
    }

    private fun updateReviewsLiveData() {
        reviewsLiveData.value = reviews
    }

    fun addReview(review: Review) {
        reviews.add(review)
        updateReviewsLiveData()
    }

    fun bindWithView(binding: FragmentShowDetailsBinding) {
        binding.collapsingToolbar.title = show?.name
        binding.showDescription.text = show?.description
        binding.showImage.setImageResource(show?.imageResourceId!!)
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