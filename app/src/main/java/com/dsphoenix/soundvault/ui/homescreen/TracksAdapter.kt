package com.dsphoenix.soundvault.ui.homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.databinding.TrackViewHolderBinding

class TracksAdapter : RecyclerView.Adapter<TracksAdapter.ViewHolder>() {

    private var items: List<Track> = emptyList()

    var onItemClickListener: ((Track) -> Unit)? = null
    var onAddTrackToQueueClickListener: ((Track) -> Unit)? = null

    private fun getItem(position: Int) = items[position]
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TrackViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding, onItemClickListener, onAddTrackToQueueClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(newItems: List<Track>) {
        val diff = DiffUtil.calculateDiff(TracksCallback(items, newItems))
        diff.dispatchUpdatesTo(this)
        items = newItems
    }

    class ViewHolder(
        private val binding: TrackViewHolderBinding,
        private val trackClickListener: ((Track) -> Unit)?,
        private val addToQueueClickListener: ((Track) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Track) {
            binding.apply {
                root.setOnClickListener { trackClickListener?.invoke(track) }
                ibAddToQueue.setOnClickListener { addToQueueClickListener?.invoke(track) }
                tvName.text = root.context.getString(
                    R.string.track_viewholder_title,
                    track.authorName,
                    track.name
                )

                Glide.with(root.context).load(track.imagePath).into(ivCover)

                if (track.genres.isNullOrEmpty())
                    tvGenres.visibility = View.GONE
                else
                    tvGenres.text = track.genres.joinToString(", ")
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
