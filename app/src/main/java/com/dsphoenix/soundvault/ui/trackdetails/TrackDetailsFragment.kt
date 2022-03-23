package com.dsphoenix.soundvault.ui.trackdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.dsphoenix.soundvault.databinding.TrackDetailsFragmentBinding
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment

class TrackDetailsFragment :
    ViewBindingFragment<TrackDetailsFragmentBinding>(TrackDetailsFragmentBinding::inflate) {
    private val viewModel: TrackDetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val trackId = arguments?.getString(TRACK_ID_STRING_KEY)
        viewModel.setTrackId(trackId!!)
        setupView()
    }

    private fun setupView() {
        binding.apply {
            viewModel.track.observe(viewLifecycleOwner) { track ->
                tvAuthorName.text = track.authorName
                tvTrackName.text = track.name
                tvGenres.text = track.genres?.joinToString(", ") {it}
            }
            viewModel.imageRef.observe(viewLifecycleOwner) { ref ->
                ref?.let { ivTrackCover.loadImage(requireContext(), it) }
            }
        }
    }

    companion object {
        private const val TRACK_ID_STRING_KEY = "TRACK_ID"
        fun createInstance(trackId: String) = TrackDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(TRACK_ID_STRING_KEY, trackId)
            }
        }
    }
}