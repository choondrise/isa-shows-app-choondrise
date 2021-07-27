package com.choondrise.shows_hrvoje_brajko.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.choondrise.shows_hrvoje_brajko.databinding.ViewReviewItemBinding
import com.choondrise.shows_hrvoje_brajko.models.Review

class ReviewsAdapter(
    private var items: List<Review>,
) : RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val binding = ViewReviewItemBinding.inflate(LayoutInflater.from(parent.context))
        return ReviewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addItem(item: Review) {
        items = items + item
        notifyItemInserted(items.size)
    }

    inner class ReviewsViewHolder(private val binding: ViewReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            binding.username.text = review.user.email
            binding.numberOfStars.text = review.rating.toString()
            if (review.comment.trim().isEmpty()) {
                binding.reviewDescription.isVisible = false
            } else {
                binding.reviewDescription.isVisible = true
                binding.reviewDescription.text = review.comment
            }
        }
    }
}