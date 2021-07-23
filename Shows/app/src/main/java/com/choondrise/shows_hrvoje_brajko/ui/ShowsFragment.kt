package com.choondrise.shows_hrvoje_brajko.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.choondrise.shows_hrvoje_brajko.R
import com.choondrise.shows_hrvoje_brajko.databinding.DialogAddReviewBinding
import com.choondrise.shows_hrvoje_brajko.databinding.DialogProfileDetailsBinding
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentShowsBinding
import com.choondrise.shows_hrvoje_brajko.model.Review
import com.choondrise.shows_hrvoje_brajko.model.Show
import com.choondrise.shows_hrvoje_brajko.model.ShowsViewModel
import com.choondrise.shows_hrvoje_brajko.model.preparePermissionsContract
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!

    private var adapter: ShowsAdapter? = null
    private lateinit var username: String

    private val args: ShowsFragmentArgs by navArgs()
    private val viewModel: ShowsViewModel by viewModels()

    private val cameraPermissionForTakingProfilePicture = preparePermissionsContract(onPermissionsGranted = {
        Toast.makeText(activity, "dao si mi permission a sad samo trebam otvoriti kameru", Toast.LENGTH_SHORT).show()
    })

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        initBackButton()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.rememberMe) {
            loggedIn()
        }

        username = args.username.split("@")[0]

        binding.showsEmptyText.isVisible = false
        binding.showsEmptyImage.isVisible = false

        initViewModel()
        initShowHideButton()
        initProfileIconButton()
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
            navigateToShowDetailsFragment(name, description, imageResourceId)
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

    private fun initProfileIconButton() {
        binding.logoutButton.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val dialog = activity?.let { BottomSheetDialog(it) }
        val dialogBinding = DialogProfileDetailsBinding.inflate(layoutInflater)
        dialog?.setContentView(dialogBinding.root)
        dialogBinding.username.text = args.username
        initChangeProfilePhotoButton(dialog, dialogBinding)
        initLogoutButton(dialog, dialogBinding)
        dialog?.show()
    }

    private fun navigateToShowDetailsFragment(name: String, description: String, imageResourceId: Int) {
        val action = ShowsFragmentDirections.actionShowsToShowDetails(username, name, description, imageResourceId)
        findNavController().navigate(action)
    }

    private fun navigateToLoginFragment() {
        val action = ShowsFragmentDirections.actionShowsToLogin()
        findNavController().navigate(action)
    }

    private fun loggedIn() {
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(prefs.edit()) {
            putBoolean("ALREADY_LOGGED_IN", true)
            putString("USERNAME", args.username)
            putString("PASSWORD", args.password)
            apply()
        }
    }

    private fun clearPreferences() {
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(prefs.edit()) {
            putBoolean("ALREADY_LOGGED_IN", false)
            putString("USERNAME", null)
            putString("PASSWORD", null)
            apply()
        }
    }

    private fun initLogoutButton(dialog: BottomSheetDialog?, dialogBinding: DialogProfileDetailsBinding) {
        dialogBinding.logoutButton.setOnClickListener {
            clearPreferences()
            navigateToLoginFragment()
            dialog?.dismiss()
        }
    }

    private fun initChangeProfilePhotoButton(dialog: BottomSheetDialog?, dialogBinding: DialogProfileDetailsBinding) {
        dialogBinding.changeProfilePhotoButton.setOnClickListener {
            cameraPermissionForTakingProfilePicture.launch(arrayOf(Manifest.permission.CAMERA))
            dialog?.dismiss()
        }
    }

    private fun initBackButton() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}