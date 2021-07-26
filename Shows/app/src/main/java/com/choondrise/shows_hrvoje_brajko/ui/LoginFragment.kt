package com.choondrise.shows_hrvoje_brajko.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentLoginBinding
import com.choondrise.shows_hrvoje_brajko.model.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val args: LoginFragmentArgs by navArgs()
    private val loginViewModel: LoginViewModel by viewModels()

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
            navigateToShowsFragment(prefs.getString("USERNAME", "").toString(),
                prefs.getString("PASSWORD", "").toString(),
                true)
        }

        checkIfAlreadyRegistered()
        initLoginButton()
        initRegisterButton()
        initTextChangeListeners()
        initViewModel()
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
        if (loginViewModel.validateEmail(username, binding) &&
            loginViewModel.validatePassword(password, binding)) {
                loginViewModel.login(username, password)
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

    private fun onTextChange() {
        binding.loginButton.isEnabled = binding.editTextEmail.text.toString().isNotEmpty() &&
                binding.editTextPassword.text.toString().length > PASSWORD_MAX_LENGTH - 1
    }

    private fun checkIfAlreadyRegistered() {
        if (args.alreadyRegistered) {
            binding.registerButton.isVisible = false
            binding.loginTitle.text = "Registration successful!"
        }
    }

    private fun initViewModel() {
        loginViewModel.getLoginResultLiveData().observe(this.viewLifecycleOwner, { isLoginSuccessful ->
            if (isLoginSuccessful) {
                navigateToShowsFragment(binding.editTextEmail.text.toString(),
                    binding.editTextPassword.text.toString(),
                    binding.rememberMeCheckbox.isChecked)
            } else {
                Toast.makeText(activity, "Login unsuccessful", Toast.LENGTH_SHORT).show()
            }
        })
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

