package com.dsphoenix.soundvault.ui.homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.databinding.VhTrackBinding

class TracksAdapter : RecyclerView.Adapter<TracksAdapter.ViewHolder>() {

    private var items: List<Track> = emptyList()

    private fun getItem(position: Int) = items[position]
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VhTrackBinding.inflate(
            LayoutInflater.from(parent.context)
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

    class ViewHolder(private val binding: VhTrackBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Track) {
            binding.tvName.text = track.name

            if (track.description.isNullOrEmpty())
                binding.tvDescription.visibility = View.GONE
            else
                binding.tvDescription.text = track.description
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
