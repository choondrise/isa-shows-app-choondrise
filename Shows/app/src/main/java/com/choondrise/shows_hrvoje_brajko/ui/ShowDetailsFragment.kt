package com.choondrise.shows_hrvoje_brajko.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.choondrise.shows_hrvoje_brajko.databinding.DialogAddReviewBinding
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentShowDetailsBinding
import com.choondrise.shows_hrvoje_brajko.model.ShowDetailsViewModel
import com.choondrise.shows_hrvoje_brajko.models.Review
import com.choondrise.shows_hrvoje_brajko.models.Show
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ShowDetailsFragmentArgs by navArgs()
    private val viewModel: ShowDetailsViewModel by viewModels()

    private var adapter: ReviewsAdapter? = null

    private var username: String? = null
    private var show: Show? = null
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

        getShowFromArgs()
        initViewModel()
        initReviewButton()
        initBackButton()
    }

    private fun initViewModel() {
        viewModel.getReviewsLiveData().observe(viewLifecycleOwner, { reviews ->
            initRecycleView(reviews)
        })

        viewModel.initShowLiveData(show!!)
        viewModel.getShowLiveData().observe(viewLifecycleOwner, {
            viewModel.bindWithView(binding)
        })
    }

    private fun initRecycleView(reviews: List<Review>) {
        binding.reviewRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = ReviewsAdapter(reviews) { _, _, _, _ -> Unit}
        binding.reviewRecycler.adapter = adapter
        binding.reviewRecycler.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding.ratingBar.isVisible = false
        binding.ratingTotal.isVisible = false
    }

    /*private fun showBottomSheet() {
        val dialog = activity?.let { BottomSheetDialog(it) }
        val dialogBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog?.setContentView(dialogBinding.root)
        dialogBinding.submitButton.setOnClickListener {
            val review = Review(username.toString(),
                dialogBinding.editTextReview.text.toString().trim(),
                dialogBinding.ratingBar.rating.toInt())

            adapter?.addItem(review)
            viewModel.addReview(review)
            dialog?.dismiss()
            totalRating += dialogBinding.ratingBar.rating.toInt()
            viewModel.updateRating(binding, totalRating, adapter)
        }
        dialog?.show()
    }*/

    private fun getShowFromArgs() {
        username = args.username
        show = Show(args.id, args.averageRating, args.description, args.imageUrl, args.noOfReviews, args.title)
    }

    private fun initReviewButton() {
        binding.reviewButton.setOnClickListener {
            // showBottomSheet()
        }
    }

    private fun initBackButton() {
        (activity as AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity)?.supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}