package com.dsphoenix.soundvault.ui.homescreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.dsphoenix.soundvault.databinding.HomeScreenFragmentBinding
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : ViewBindingFragment<HomeScreenFragmentBinding>(HomeScreenFragmentBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        binding.rvTracks.adapter = TracksAdapter()
        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            (binding.rvTracks.adapter as TracksAdapter).setData(tracks)
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