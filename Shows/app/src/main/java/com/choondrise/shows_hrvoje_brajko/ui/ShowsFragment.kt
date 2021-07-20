package com.choondrise.shows_hrvoje_brajko.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.choondrise.shows_hrvoje_brajko.R
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentShowsBinding
import com.choondrise.shows_hrvoje_brajko.model.Show

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!

    private var adapter: ShowsAdapter? = null
    private lateinit var username: String

    private val args: ShowsFragmentArgs by navArgs()

    private val shows = listOf(
        Show(0, "Zabranjena ljubav",
            "Brother and sister twins separated at birth meet eachother nearly " +
                    "two decades later not knowing about their blood relation. The two fall in love and that is only " +
                    "the peak of the iceberg for what is yet to come.",
            R.drawable.zabranjena_ljubav
        ),
        Show(1, "Vecernja skola",
            "Humorous tv show about a teacher and his middle-aged students " +
                    "who attend night school.",
            R.drawable.vecernja_skola
        ),
        Show(2, "Nasa mala klinika",
            "Sitcom about the staff, patients, guests and all kinds of different " +
                    "events in a small clinic at the end of a town.",
            R.drawable.nasa_mala_klinika
        )
    )

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

        initRecycleView()
        initShowHideButton()
    }
    private fun initRecycleView() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}