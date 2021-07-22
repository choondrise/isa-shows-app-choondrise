package com.choondrise.shows_hrvoje_brajko.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.choondrise.shows_hrvoje_brajko.R
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentShowsBinding
import com.choondrise.shows_hrvoje_brajko.model.Show
import com.choondrise.shows_hrvoje_brajko.model.ShowsViewModel

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!

    private var adapter: ShowsAdapter? = null
    private lateinit var username: String

    private val args: ShowsFragmentArgs by navArgs()
    private val viewModel: ShowsViewModel by viewModels()

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = args.username.split("@")[0]

        binding.showsEmptyText.isVisible = false
        binding.showsEmptyImage.isVisible = false

        initViewModel()
        initShowHideButton()
        initLogoutButton()
    }

    private fun initViewModel() {
        viewModel.getShowsLiveData().observe(viewLifecycleOwner, { shows ->
            initRecycleView(shows)
        })
        viewModel.initShows()
    }

    private fun initRecycleView(shows: List<Show>) {
        binding.showRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = ShowsAdapter(shows) { name, description, imageResourceId ->
            val action = ShowsFragmentDirections.actionShowsToShowDetails(username, name, description, imageResourceId)
            findNavController().navigate(action)
        }
        binding.showRecycler.adapter = adapter
    }


    private fun initShowHideButton() {
        binding.fab.setOnClickListener {
            if (binding.showRecycler.isVisible) {
                binding.showRecycler.isVisible = false
                binding.showsEmptyImage.isVisible = true
                binding.showsEmptyText.isVisible = true
            } else {
                binding.showRecycler.isVisible = true
                binding.showsEmptyImage.isVisible = false
                binding.showsEmptyText.isVisible = false
            }
        }
    }

    private fun initLogoutButton() {
        binding.logoutButton.setOnClickListener {
            val action = ShowsFragmentDirections.actionShowsToLogin()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}