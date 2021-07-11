package com.choondrise.shows_hrvoje_brajko

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.choondrise.shows_hrvoje_brajko.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    companion object {
        private const val EXTRA_EMAIL = "EXTRA_EMAIL"
        private const val EXTRA_PASSWORD = "EXTRA_PASSWORD"

        fun buildIntent(activity: Activity, email: String, password: String) : Intent {
            val intent = Intent(activity, WelcomeActivity::class.java)
            intent.putExtra(EXTRA_EMAIL, email)
            intent.putExtra(EXTRA_PASSWORD, password)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        val email = intent.extras?.getString(EXTRA_EMAIL)
        val password = intent.extras?.getString(EXTRA_PASSWORD)
        val welcome = "Welcome, "

        val emailName = email?.split("@")?.get(0)

        binding.welcomeText.text = welcome.plus(emailName.toString()).plus("!")
        setContentView(binding.root)
    }
}