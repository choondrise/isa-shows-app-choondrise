package com.choondrise.shows_hrvoje_brajko.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.choondrise.shows_hrvoje_brajko.R

class ShowsViewModel : ViewModel() {

    private val shows = mutableListOf<Show>(
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

    private val showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    fun getShowsLiveData(): LiveData<List<Show>> {
        return showsLiveData
    }

    fun initShows() {
        showsLiveData.value = shows
    }

}