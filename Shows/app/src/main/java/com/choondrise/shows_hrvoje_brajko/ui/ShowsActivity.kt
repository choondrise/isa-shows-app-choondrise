package com.choondrise.shows_hrvoje_brajko.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.choondrise.shows_hrvoje_brajko.R
import com.choondrise.shows_hrvoje_brajko.databinding.ActivityShowsBinding
import com.choondrise.shows_hrvoje_brajko.databinding.ViewShowItemBinding
import com.choondrise.shows_hrvoje_brajko.model.Show

class ShowsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowsBinding
    private var adapter: ShowsAdapter? = null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var username: String

    companion object {
        private const val EXTRA_EMAIL = "EXTRA_EMAIL"
        private const val EXTRA_PASSWORD = "EXTRA_PASSWORD"

        fun buildIntent(activity: Activity, email: String, password: String) : Intent {
            val intent = Intent(activity, ShowsActivity::class.java)
            intent.putExtra(EXTRA_EMAIL, email)
            intent.putExtra(EXTRA_PASSWORD, password)
            return intent
        }
    }

    private val shows = listOf(
        Show(0, "Zabranjena ljubav",
            "Brother and sister twins separated at birth meet eachother nearly " +
                "two decades later not knowing about their blood relation. The two fall in love and that is only " +
                "the peak of the iceberg for what is yet to come.",
            R.drawable.zabranjena_ljubav
        ),
        Show(1, "Vecernja skola",
            "Humorous tv show about a teacher and his middle-aged students " +
                "who attend night school.",
            R.drawable.vecernja_skola
        ),
        Show(2, "Nasa mala klinika",
            "Sitcom about the staff, patients, guests and all kinds of different " +
                "events in a small clinic at the end of a town.",
            R.drawable.nasa_mala_klinika
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)

        email = intent.extras?.getString(EXTRA_EMAIL).toString()
        password = intent.extras?.getString(EXTRA_PASSWORD).toString()
        username = email.split("@")[0]

        // set EmptyImage and EmptyText to invisible on start
        binding.showsEmptyText.isVisible = false
        binding.showsEmptyImage.isVisible = false

        setContentView(binding.root)

        initRecycleView()
        initShowHideButton()
    }

    private fun initRecycleView() {
        binding.showRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ShowsAdapter(shows) { showID, name, description, imageResourceId ->
            val intent = ShowDetailsActivity.buildIntent(
                this,
                username,
                showID,
                name,
                description,
                imageResourceId
            )
            startActivity(intent)
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
}