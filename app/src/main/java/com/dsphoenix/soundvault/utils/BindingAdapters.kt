package com.dsphoenix.soundvault.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.ui.homescreen.BindableTracksAdapter

@BindingAdapter("items")
fun bindTracks(recyclerView: RecyclerView, items: List<Track>?) {
    val adapter: BindableTracksAdapter =
        if (recyclerView.adapter != null && recyclerView.adapter is BindableTracksAdapter) {
            recyclerView.adapter as BindableTracksAdapter
        } else {
            BindableTracksAdapter()
        }
    recyclerView.adapter = adapter
    adapter.updateItems(items)
}
