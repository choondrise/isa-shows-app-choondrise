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

            if (validateEmail(binding.editTextEmail.text.toString())) {
                if (validatePassword(binding.editTextPassword.text.toString())) {
                    val intent = WelcomeActivity.buildIntent(
                        this,
                        binding.editTextEmail.text.toString(),
                        binding.editTextPassword.text.toString())
                    startActivity(intent)
                } else {
                    binding.editTextPassword.error = "Password needs to contain at least 6 characters"
                }
            } else {
                binding.editTextEmail.error = "Email needs to contain at least 1 character and '@' sign"
            }
        }
    }

    private fun validateEmail(email: String) : Boolean {
        if (email.isEmpty()) return false

        val regex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)".toRegex()
        return email.matches(regex)
    }

    private fun validatePassword(password: String) : Boolean {
        if (password.isEmpty()) return false
        return password.length > 5
    }
}
