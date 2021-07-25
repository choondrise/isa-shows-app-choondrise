package com.choondrise.shows_hrvoje_brajko.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentRegistrationBinding
import com.choondrise.shows_hrvoje_brajko.model.RegistrationViewModel

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRegisterButton()
        initTextChangeListeners()

        // val prefs = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        // val alreadyLoggedIn = prefs.getBoolean("ALREADY_LOGGED_IN", false)
    }

    private fun initRegisterButton() {
        binding.registerButton.setOnClickListener {
            register()
        }
    }

    private fun register() {
        if (registrationViewModel.validateEmail(binding.editTextEmail.text.toString(), binding) &&
                registrationViewModel.validatePassword(binding.editTextPassword.text.toString(), binding) &&
                registrationViewModel.passwordsMatch(binding)) {
            navigateToLoginFragment()
        }
    }

    private fun navigateToLoginFragment() {
        val action = RegistrationFragmentDirections.actionRegisterToLogin()
        findNavController().navigate(action)
    }

    private fun initTextChangeListeners() {
        binding.editTextEmail.doOnTextChanged { _, _, _, _ ->
            onTextChange()
        }

        binding.editTextPassword.doOnTextChanged { _, _, _, _ ->
            onTextChange()
        }
    }

    private fun onTextChange() {
        binding.registerButton.isEnabled = binding.editTextEmail.text.toString().isNotEmpty() &&
                binding.editTextPassword.text.toString().length > LoginFragment.PASSWORD_MAX_LENGTH - 1
    }

}