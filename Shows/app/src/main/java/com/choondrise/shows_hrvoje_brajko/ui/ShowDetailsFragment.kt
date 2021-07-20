package com.choondrise.shows_hrvoje_brajko.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.choondrise.shows_hrvoje_brajko.databinding.DialogAddReviewBinding
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentShowDetailsBinding
import com.choondrise.shows_hrvoje_brajko.model.Review
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ShowDetailsFragmentArgs by navArgs()

    private var adapter: ReviewsAdapter? = null

    private var reviews = listOf<Review>()

    private var username: String? = null
    private var showName: String? = null
    private var showDescription: String? = null
    private var showImageResourceId: Int? = null
    private var totalRating: Int = 0

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFromArgs()
        bindWithView()

        initRecycleView()
        initReviewButton()
        initBackButton()
    }

    private fun initRecycleView() {
        binding.reviewRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = ReviewsAdapter(reviews) { _, _, _, _ -> Unit}
        binding.reviewRecycler.adapter = adapter
        binding.reviewRecycler.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding.ratingBar.isVisible = false
        binding.ratingTotal.isVisible = false
    }

    private fun showBottomSheet() {
        val dialog = activity?.let { BottomSheetDialog(it) }
        val dialogBinding = DialogAddReviewBinding.inflate(layoutInflater)

        dialog?.setContentView(dialogBinding.root)
        dialogBinding.submitButton.setOnClickListener {
            val review = Review(username.toString(),
                dialogBinding.editTextReview.text.toString().trim(),
                dialogBinding.ratingBar.rating.toInt())
            adapter?.addItem(review)
            dialog?.dismiss()
            reviews = reviews + review
            totalRating += dialogBinding.ratingBar.rating.toInt()
            updateRating()
        }
        dialog?.show()
    }

    private fun initReviewButton() {
        binding.reviewButton.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun getFromArgs() {
        username = args.username
        showName = args.showName
        showDescription = args.showDescription
        showImageResourceId = args.imageResourceID
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
        (activity as AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity)?.supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}