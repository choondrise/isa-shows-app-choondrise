package com.choondrise.shows_hrvoje_brajko.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.choondrise.shows_hrvoje_brajko.R
import com.choondrise.shows_hrvoje_brajko.databinding.ActivityShowsBinding
import com.choondrise.shows_hrvoje_brajko.model.Show

class ShowsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowsBinding
    private var adapter: ShowsAdapter? = null

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
        Show("1", "Krim Tim 2",
            "The three police inspectors are solving cases around the city of Zagreb.",
            R.drawable.krim_tim_2
        ),
        Show("2", "Zabranjena ljubav",
            "Brother and sister twins separated at birth meet eachother nearly " +
                "two decades later not knowing about their blood relation. The two fall in love and that is only " +
                "the peak of the iceberg for what is yet to come.",
            R.drawable.zabranjena_ljubav
        ),
        Show("3", "Vecernja skola",
            "Humorous tv show about a teacher and his middle-aged students " +
                "who attend night school.",
            R.drawable.vecernja_skola
        ),
        Show("4", "Nasa mala klinika",
            "Sitcom about the staff, patients, guests and all kinds of different " +
                "events in a small clinic at the end of a town.",
            R.drawable.nasa_mala_klinika
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)

        // val email = intent.extras?.getString(EXTRA_EMAIL)
        // val password = intent.extras?.getString(EXTRA_PASSWORD)

        setContentView(binding.root)

        initRecycleView()
    }

    private fun initRecycleView() {
        binding.showRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ShowsAdapter(shows) { name ->
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
        }
        binding.showRecycler.adapter = adapter
    }
}