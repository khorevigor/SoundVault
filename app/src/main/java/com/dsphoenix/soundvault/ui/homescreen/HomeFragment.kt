package com.dsphoenix.soundvault.ui.homescreen

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.databinding.HomeScreenFragmentBinding
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : ViewBindingFragment<HomeScreenFragmentBinding>(HomeScreenFragmentBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    var onTrackClickListener: ((Track) -> Unit)? = null
    var onAddToQueueClickListener: ((Track) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        viewModel.fetchTracks()
    }

    private fun setupView() {
        binding.apply {
            rvTracks.adapter = TracksAdapter()
            val decorator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            decorator.setDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.divider,
                    null
                )!!
            )
            rvTracks.addItemDecoration(decorator)

            viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
                (rvTracks.adapter as TracksAdapter).apply {
                    setData(tracks)
                    onItemClickListener = onTrackClickListener
                    onAddTrackToQueueClickListener = onAddToQueueClickListener
                }
            }

            subButton.setOnClickListener { viewModel.toggleSubscription() }
            viewModel.user.observe(viewLifecycleOwner) { user ->
                subButton.text =
                    if (user.hasSubscription == true) "Unsubscribe" else "Subscribe"
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshTracks()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    companion object {
        const val NAVIGATION_TAG = "HomeFragment"
    }
}