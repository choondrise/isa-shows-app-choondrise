package com.choondrise.shows_hrvoje_brajko.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.choondrise.shows_hrvoje_brajko.databinding.ActivityShowDetailsBinding
import com.choondrise.shows_hrvoje_brajko.databinding.DialogAddReviewBinding
import com.choondrise.shows_hrvoje_brajko.model.Review
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowDetailsBinding
    private var adapter: ReviewsAdapter? = null

    private var reviews = listOf<Review>()

    private var username: String? = null
    private var showID: Int? = null
    private var showName: String? = null
    private var showDescription: String? = null
    private var showImageResourceId: Int? = null

    companion object {
        private const val EXTRA_USERNAME = "EXTRA_USERNAME"
        private const val EXTRA_SHOW_ID = "EXTRA_SHOW_ID"
        private const val EXTRA_SHOW_NAME = "EXTRA_SHOW_NAME"
        private const val EXTRA_SHOW_DESCRIPTION = "EXTRA_SHOW_DESCRIPTION"
        private const val EXTRA_SHOW_IMAGE = "EXTRA_SHOW_IMAGE"

        private var totalRating: Int = 0

        fun buildIntent(activity: Activity,
                        username: String,
                        showID: Int,
                        showName: String,
                        showDescription: String,
                        showImageResourceId: Int) : Intent {
            val intent = Intent(activity, ShowDetailsActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, username)
            intent.putExtra(EXTRA_SHOW_ID, showID)
            intent.putExtra(EXTRA_SHOW_NAME, showName)
            intent.putExtra(EXTRA_SHOW_DESCRIPTION, showDescription)
            intent.putExtra(EXTRA_SHOW_IMAGE, showImageResourceId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        totalRating = 0

        getFromExtras()
        bindWithView()

        initRecycleView()
        initReviewButton()
        initBackButton()

        setContentView(binding.root)
    }

    private fun initRecycleView() {
        binding.reviewRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ReviewsAdapter(reviews) { _, _, _, _ -> Unit}
        binding.reviewRecycler.adapter = adapter
        binding.reviewRecycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.ratingBar.isVisible = false
        binding.ratingTotal.isVisible = false
    }

    private fun initReviewButton() {
        binding.reviewButton.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(this)
        val dialogBinding = DialogAddReviewBinding.inflate(layoutInflater)

        dialog.setContentView(dialogBinding.root)
        dialogBinding.submitButton.setOnClickListener {
            val review = Review(showID.toString().toInt(),
                username.toString(),
                dialogBinding.editTextReview.text.toString().trim(),
                dialogBinding.ratingBar.rating.toInt())
            adapter?.addItem(review)
            dialog.dismiss()
            reviews = reviews + review
            totalRating += dialogBinding.ratingBar.rating.toInt()
            updateRating()
        }
        dialog.show()
    }

    private fun getFromExtras() {
        username = intent.extras?.getString(EXTRA_USERNAME).toString()
        showID = intent.extras?.getInt(EXTRA_SHOW_ID).toString().toInt()
        showName = intent.extras?.getString(EXTRA_SHOW_NAME).toString()
        showDescription = intent.extras?.getString(EXTRA_SHOW_DESCRIPTION).toString()
        showImageResourceId = intent.extras?.getInt(EXTRA_SHOW_IMAGE).toString().toInt()
    }

    private fun bindWithView() {
        binding.collapsingToolbar.title = showName
        binding.showDescription.text = showDescription
        binding.showImage.setImageResource(showImageResourceId!!)
    }

    private fun updateRating() {
        val rating: Float = (totalRating * 1.0f / adapter!!.itemCount)
        binding.ratingTotal.isVisible = true
        binding.ratingBar.isVisible = true
        binding.ratingBar.setIsIndicator(true)
        binding.reviewsEmptyText.isVisible = false
        binding.ratingTotal.text = (adapter!!.itemCount.toString() + " REVIEWS, " + rating.toString() + " AVERAGE")
        binding.ratingBar.rating = rating
    }

    private fun initBackButton() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}