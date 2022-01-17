package com.dsphoenix.soundvault.ui.homescreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.BR
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.databinding.VhTrackBinding

class BindableTracksAdapter
    : RecyclerView.Adapter<BindableTracksAdapter.ViewHolder>() {

    private var tracksList: List<Track> = emptyList()

    private fun getItem(position: Int) = tracksList[position]
    override fun getItemCount() = tracksList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: VhTrackBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.vh_track,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun updateItems(tracks: List<Track>?) {
        tracksList = tracks ?: emptyList()
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Track) {
            binding.setVariable(BR.track, track)
        }
    }
}
