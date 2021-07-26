package com.choondrise.shows_hrvoje_brajko.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.choondrise.shows_hrvoje_brajko.R
import com.choondrise.shows_hrvoje_brajko.databinding.ViewShowItemBinding
import com.choondrise.shows_hrvoje_brajko.models.Show

class ShowsAdapter(
    private var items: List<Show>,
    private val onClickCallback: (String) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        val binding = ViewShowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ShowsViewHolder(private val binding: ViewShowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(show: Show) {
            binding.showName.text = show.title
            binding.showDescription.text = show.description
            Glide.with(context)
                .load(show.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_profile_placeholder)
                .into(binding.showImage)

            binding.root.setOnClickListener {
                onClickCallback(show.id)
            }
        }
    }
}
