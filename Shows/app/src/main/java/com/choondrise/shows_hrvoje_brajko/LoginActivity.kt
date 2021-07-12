package com.choondrise.shows_hrvoje_brajko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.doOnTextChanged
import com.choondrise.shows_hrvoje_brajko.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

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
        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.loginButton.isEnabled = binding.editTextEmail.text.toString().isNotEmpty() &&
                        binding.editTextPassword.text.toString().length > 5
            }
        })
        /*binding.editTextEmail.doOnTextChanged { text, start, before, count ->

        }*/
        binding.editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.loginButton.isEnabled = binding.editTextEmail.text.toString().isNotEmpty() &&
                        binding.editTextPassword.text.toString().length > 5
            }
        })
    }

    private fun validateEmail(email: String) : Boolean {
        val regex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)".toRegex()
        if (email.isEmpty()) {
            binding.emailInput.isErrorEnabled = true
            binding.emailInput.error = "Email needs to contain at least 1 character"
            return false
        } else if (!email.matches(regex)){
            binding.emailInput.isErrorEnabled = true
            binding.emailInput.error = "Email does not match email regex"
            return false
        } else {
            binding.emailInput.isErrorEnabled = false
            binding.emailInput.error = null
            return true
        }
    }

    private fun validatePassword(password: String) : Boolean {
        if (password.length < 6) {
            binding.emailInput.isErrorEnabled = true
            binding.passwordInput.error = "Password needs to contain at least 5 characters"
            return false
        } else {
            binding.emailInput.isErrorEnabled = false
            binding.passwordInput.error = null
            return true
        }
    }
}
