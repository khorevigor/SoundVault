package com.dsphoenix.soundvault.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.HomeScreenFragmentLayoutBinding
import com.dsphoenix.soundvault.databinding.UploadScreenFragmentLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: HomeScreenFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeScreenFragmentLayoutBinding.inflate(inflater, container, false)

        setupView()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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