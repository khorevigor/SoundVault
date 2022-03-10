package com.dsphoenix.soundvault.ui.homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.databinding.TrackViewHolderBinding

class TracksAdapter : RecyclerView.Adapter<TracksAdapter.ViewHolder>() {

    private var items: List<Track> = emptyList()

    private fun getItem(position: Int) = items[position]
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TrackViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(newItems: List<Track>) {
        val diff = DiffUtil.calculateDiff(TracksCallback(items, newItems))
        diff.dispatchUpdatesTo(this)
        items = newItems
    }

    class ViewHolder(private val binding: TrackViewHolderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Track) {
            binding.apply {
                tvName.text = root.context.getString(
                    R.string.track_viewholder_title,
                    track.authorName,
                    track.name
                )

                if (track.genres.isNullOrEmpty())
                    tvGenres.visibility = View.GONE
                else
                    tvGenres.text = track.genres.joinToString( ", ")
            }
        }
    }
}

    class TracksCallback(
        private val oldList: List<Track>,
        private val newList: List<Track>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].name == newList[newItemPosition].name

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
