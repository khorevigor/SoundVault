package com.dsphoenix.soundvault.ui.homescreen

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.HomeScreenFragmentBinding
import com.dsphoenix.soundvault.ui.MainActivity
import com.dsphoenix.soundvault.utils.navigation.NavigationController
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : ViewBindingFragment<HomeScreenFragmentBinding>(HomeScreenFragmentBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    var onTrackClickListener: ((String) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        binding.rvTracks.adapter = TracksAdapter()
        val decorator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        decorator.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.divider, null)!!)
        binding.rvTracks.addItemDecoration(decorator)

        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            (binding.rvTracks.adapter as TracksAdapter).apply {
                setData(tracks)
                onItemClickListener = onTrackClickListener
            }
        }
        binding.subButton.setOnClickListener { viewModel.toggleSubscription() }
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.subButton.text =
                if (user.hasSubscription == true) "Unsubscribe" else "Subscribe"
        }
    }

    companion object {
        const val navigationTag = "HomeFragment"
    }
}