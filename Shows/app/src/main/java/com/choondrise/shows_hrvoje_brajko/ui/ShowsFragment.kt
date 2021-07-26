package com.choondrise.shows_hrvoje_brajko.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.choondrise.shows_hrvoje_brajko.databinding.DialogProfileDetailsBinding
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentShowsBinding
import com.choondrise.shows_hrvoje_brajko.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.choondrise.shows_hrvoje_brajko.R
import com.choondrise.shows_hrvoje_brajko.models.Show

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!

    private var adapter: ShowsAdapter? = null
    private lateinit var username: String

    private val args: ShowsFragmentArgs by navArgs()
    private val viewModel: ShowsViewModel by viewModels()
    private lateinit var profilePhotoUri: Uri

    private val cameraPermissionForTakingProfilePicture =
        preparePermissionsContract(onPermissionsGranted = {
            startCameraContractUsingUri()
        })

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val CAMERA_ERROR_MESSAGE = "An error occurred while opening camera app"
        private const val ALERT_DIALOG_MESSAGE = "Are you sure you want to log out?"
        private const val ALERT_DIALOG_POSITIVE_TEXT = "Yes"
        private const val ALERT_DIALOG_NEGATIVE_TEXT = "Cancel"
    }

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
        viewModel.listShows()
        viewModel.getShowsLiveData().observe(viewLifecycleOwner, { shows ->
            initRecycleView(shows)
        })
    }

    private fun initRecycleView(shows: List<Show>) {
        binding.showRecycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = activity?.let {
            ShowsAdapter(shows, { id ->
                navigateToShowDetailsFragment(id)
            }, requireActivity())

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
        changeProfileIcon(dialogBinding)
        initChangeProfilePhotoButton(dialog, dialogBinding)
        initLogoutButton(dialog, dialogBinding)
        dialog?.show()
    }

    private fun navigateToShowDetailsFragment(
        id: String
    ) {
        val prefs = activity?.getSharedPreferences("LOGIN_CREDENTIALS", Context.MODE_PRIVATE) ?: return
        val action = ShowsFragmentDirections.actionShowsToShowDetails(
            prefs.getString("UID", "")!!,
            username,
            id
        )
        findNavController().navigate(action)
    }

    private fun navigateToLoginFragment() {
        val action = ShowsFragmentDirections.actionShowsToLogin()
        action.alreadyRegistered = false
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

    private fun initLogoutButton(
        dialog: BottomSheetDialog?,
        dialogBinding: DialogProfileDetailsBinding
    ) {
        dialogBinding.logoutButton.setOnClickListener {
            showLogoutAlertDialog()
            dialog?.dismiss()
        }
    }

    private fun initChangeProfilePhotoButton(
        dialog: BottomSheetDialog?,
        dialogBinding: DialogProfileDetailsBinding
    ) {
        dialogBinding.changeProfilePhotoButton.setOnClickListener {
            cameraPermissionForTakingProfilePicture.launch(arrayOf(Manifest.permission.CAMERA))
            dialog?.dismiss()
        }
    }

    private fun initBackButton() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })
    }

    private fun startCameraContractUsingUri() {
        val profilePhotoFile = activity?.let { FileUtil.createImageFile(it) }
        profilePhotoUri = FileProvider.getUriForFile(
            requireActivity(),
            "com.choondrise.shows_hrvoje_brajko" + ".fileprovider",
            profilePhotoFile!!
        )
        dispatchTakePictureIntent(profilePhotoUri)
    }

    private fun dispatchTakePictureIntent(profilePhotoUri: Uri) {
        val takePictureIntent =
            ActivityResultContracts.TakePicture().createIntent(requireActivity(), profilePhotoUri)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                activity,
                CAMERA_ERROR_MESSAGE,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            changeLogoutButtonIcon()
        }
    }

    private fun changeProfileIcon(dialogBinding: DialogProfileDetailsBinding) {

        if (this::profilePhotoUri.isInitialized) {
            Glide.with(this)
                .load(profilePhotoUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .circleCrop()
                .error(R.drawable.ic_profile_placeholder)
                .into(dialogBinding.profileIcon)
        } else {
            dialogBinding.profileIcon.setImageResource(R.drawable.ic_profile_placeholder)
        }
    }

    private fun changeLogoutButtonIcon() {
        Glide.with(this)
            .load(profilePhotoUri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .circleCrop()
            .into(binding.logoutButton)
    }

    private fun showLogoutAlertDialog() {
        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.setMessage(ALERT_DIALOG_MESSAGE)
            .setPositiveButton(ALERT_DIALOG_POSITIVE_TEXT) { _, _ ->
                clearPreferences()
                navigateToLoginFragment()
            }
            .setNegativeButton(ALERT_DIALOG_NEGATIVE_TEXT) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}