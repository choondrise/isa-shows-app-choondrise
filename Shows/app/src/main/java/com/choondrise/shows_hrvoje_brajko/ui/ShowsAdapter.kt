package com.choondrise.shows_hrvoje_brajko.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.choondrise.shows_hrvoje_brajko.databinding.ViewShowItemBinding
import com.choondrise.shows_hrvoje_brajko.model.Show

class ShowsAdapter(
    private var items: List<Show>,
    private val onClickCallback: (Int, String, String, Int) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        val binding = ViewShowItemBinding.inflate(LayoutInflater.from(parent.context))
        return ShowsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ShowsViewHolder(private val binding: ViewShowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Show) {
            binding.showName.text = item.name
            binding.showDescription.text = item.description
            binding.showImage.setImageResource(item.imageResourceId)

            binding.root.setOnClickListener {
                onClickCallback(item.ID, item.name, item.description, item.imageResourceId)
            }
        }
    }
}
