package com.choondrise.shows_hrvoje_brajko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.doOnTextChanged
import com.choondrise.shows_hrvoje_brajko.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val PASSWORD_MAX_LENGTH = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLoginButton()
        initTextChangeListeners()
    }

    private fun initLoginButton() {
        binding.loginButton.setOnClickListener {

            if (validateEmail(binding.editTextEmail.text.toString()) &&
                validatePassword(binding.editTextPassword.text.toString())) {
                val intent = WelcomeActivity.buildIntent(
                    this,
                    binding.editTextEmail.text.toString(),
                    binding.editTextPassword.text.toString()
                )
                startActivity(intent)
            }
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
            binding.emailInput.isErrorEnabled = false
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
            binding.emailInput.isErrorEnabled = false
            binding.passwordInput.error = null
            true
        }
    }

    private fun onTextChange() {
        binding.loginButton.isEnabled = binding.editTextEmail.text.toString().isNotEmpty() &&
                binding.editTextPassword.text.toString().length > PASSWORD_MAX_LENGTH - 1
    }
}
