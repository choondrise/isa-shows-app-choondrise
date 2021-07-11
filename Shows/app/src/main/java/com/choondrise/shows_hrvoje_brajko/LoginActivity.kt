package com.choondrise.shows_hrvoje_brajko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.choondrise.shows_hrvoje_brajko.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLoginButton()
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

    private fun validateEmail(email: String) : Boolean {
        val regex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)".toRegex()
        if (email.length < 2) {
            binding.emailInput.error = "Email needs to contain at least 1 character"
            return false
        } else if (!email.matches(regex)){
            binding.emailInput.error = "Email does not match email regex"
            return false
        } else {
            binding.emailInput.error = null
            return true
        }
    }

    private fun validatePassword(password: String) : Boolean {
        if (password.length < 6) {
            binding.passwordInput.error = "Password needs to contain at least 5 characters"
            return false
        } else {
            binding.passwordInput.error = null
            return true
        }
    }
}
