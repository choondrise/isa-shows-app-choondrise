package com.choondrise.shows_hrvoje_brajko.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.choondrise.shows_hrvoje_brajko.R
import com.choondrise.shows_hrvoje_brajko.databinding.DialogAddReviewBinding
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentShowDetailsBinding
import com.choondrise.shows_hrvoje_brajko.model.ShowDetailsViewModel
import com.choondrise.shows_hrvoje_brajko.models.Review
import com.choondrise.shows_hrvoje_brajko.models.Show
import com.choondrise.shows_hrvoje_brajko.models.User
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ShowDetailsFragmentArgs by navArgs()
    private val viewModel: ShowDetailsViewModel by viewModels()

    private var adapter: ReviewsAdapter? = null
    private var show: Show? = null
    private var review: Review? = null

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

        initViewModel()
        initReviewButton()
        initBackButton()
        initRecycleView(listOf())
    }

    private fun initViewModel() {
        viewModel.displayShow(args.id)
        viewModel.listReviews(args.id.toInt())

        viewModel.getReviewsLiveData().observe(viewLifecycleOwner, { reviews ->
            initRecycleView(reviews)
        })

        viewModel.getReviewLiveData().observe(viewLifecycleOwner, {
            review = viewModel.getReviewLiveData().value
            review?.let { adapter?.addItem(it) }
        })

        viewModel.getShowLiveData().observe(viewLifecycleOwner, { showOther ->
            show = showOther
            bindWithView()
        })
    }

    private fun initRecycleView(reviews: List<Review>) {
        binding.reviewRecycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = ReviewsAdapter(reviews)
        binding.reviewRecycler.adapter = adapter
        binding.reviewRecycler.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun showBottomSheet() {
        val dialog = activity?.let { BottomSheetDialog(it) }
        val dialogBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog?.setContentView(dialogBinding.root)
        dialogBinding.submitButton.setOnClickListener {
            addReview(dialogBinding)
            dialog?.dismiss()
        }
        dialog?.show()
    }

    private fun initReviewButton() {
        binding.reviewButton.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun bindWithView() {
        binding.collapsingToolbar.title = show?.title
        binding.showDescription.text = show?.description
        if (show!!.averageRating != null) {
            binding.ratingBar.rating = show?.averageRating!!
            binding.reviewsEmptyText.isVisible = false
            binding.ratingTotal.isVisible = true
            binding.ratingTotal.text = adapter?.itemCount.toString() + " REVIEWS, " + show?.averageRating.toString() + " AVERAGE"
            binding.ratingBar.isVisible = true
            binding.ratingBar.setIsIndicator(true)
            binding.reviewsEmptyText.isVisible = false
        } else {
            binding.ratingBar.isVisible = false
            binding.ratingTotal.isVisible = false
        }
        Glide.with(this)
            .load(show?.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .error(R.drawable.ic_profile_placeholder)
            .into(binding.showImage)
    }

    private fun initBackButton() {
        (activity as AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity)?.supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun addReview(dialogBinding: DialogAddReviewBinding) {
        viewModel.addReview(
            dialogBinding.ratingBar.rating.toInt(),
            dialogBinding.editTextReview.text.toString(),
            args.id.toInt()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}