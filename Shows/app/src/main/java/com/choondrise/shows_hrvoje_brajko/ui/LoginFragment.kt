package com.choondrise.shows_hrvoje_brajko.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val PASSWORD_MAX_LENGTH = 6
    }

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val alreadyLoggedIn = prefs.getBoolean("ALREADY_LOGGED_IN", false)
        if (alreadyLoggedIn) {
            navigateToShowsFragment(prefs.getString("USERNAME", "").toString(), prefs.getString("PASSWORD", "").toString(), true)
        }

        initLoginButton()
        initRegisterButton()
        initTextChangeListeners()
    }

    private fun initLoginButton() {
        binding.loginButton.setOnClickListener {
            login(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString())
        }
    }

    private fun initRegisterButton() {
        binding.registerButton.setOnClickListener {
            navigateToRegistrationFragment()
        }
    }

    private fun login(username: String, password: String) {
        if (validateEmail(binding.editTextEmail.text.toString()) &&
            validatePassword(binding.editTextPassword.text.toString())) {
            navigateToShowsFragment(username, password, binding.rememberMeCheckbox.isChecked)
        }
    }

    private fun initTextChangeListeners() {
        binding.editTextEmail.doOnTextChanged { _, _, _, _ ->
            onTextChange()
        }

        binding.editTextPassword.doOnTextChanged { _, _, _, _ ->
            onTextChange()
        }
    }

    private fun validateEmail(email: String) : Boolean {
        val regex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)".toRegex()
        return if (email.isEmpty()) {
            binding.emailInput.isErrorEnabled = true
            binding.emailInput.error = "Email needs to contain at least 1 character"
            false
        } else if (!email.matches(regex)){
            binding.emailInput.isErrorEnabled = true
            binding.emailInput.error = "Email does not match email regex"
            false
        } else {
            binding.emailInput.error = null
            true
        }
    }

    private fun validatePassword(password: String) : Boolean {
        return if (password.length < PASSWORD_MAX_LENGTH) {
            binding.emailInput.isErrorEnabled = true
            binding.passwordInput.error = "Password needs to contain at least 5 characters"
            false
        } else {
            binding.passwordInput.error = null
            true
        }
    }

    private fun onTextChange() {
        binding.loginButton.isEnabled = binding.editTextEmail.text.toString().isNotEmpty() &&
                binding.editTextPassword.text.toString().length > PASSWORD_MAX_LENGTH - 1
    }

    private fun navigateToShowsFragment(username: String, password: String, rememberMe: Boolean) {
        val action = LoginFragmentDirections.actionLoginToShows(username, password, rememberMe)
        findNavController().navigate(action)
    }

    private fun navigateToRegistrationFragment() {
        val action = LoginFragmentDirections.actionLoginToRegister()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}