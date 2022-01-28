package com.dsphoenix.soundvault.ui.homescreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.dsphoenix.soundvault.databinding.HomeScreenFragmentLayoutBinding
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : ViewBindingFragment<HomeScreenFragmentLayoutBinding>(HomeScreenFragmentLayoutBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        binding.rvTracks.adapter = TracksAdapter()
        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            (binding.rvTracks.adapter as TracksAdapter).setData(tracks)
        }
    }

    companion object {
        const val navigationTag = "HomeFragment"
    }
}