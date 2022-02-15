package com.dsphoenix.soundvault.ui.userscreen

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.UserProfileFragmentBinding
import com.dsphoenix.soundvault.ui.homescreen.HomeViewModel
import com.dsphoenix.soundvault.ui.homescreen.TracksAdapter
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment: ViewBindingFragment<UserProfileFragmentBinding>(UserProfileFragmentBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        binding.rvTracks.adapter = TracksAdapter()
        val decorator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        decorator.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.divider, null)!!)
        binding.rvTracks.addItemDecoration(decorator)

        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            (binding.rvTracks.adapter as TracksAdapter).setData(tracks)
        }
    }
}