package com.dsphoenix.soundvault.ui.trackdetails

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.TrackDetailsFragmentBinding
import com.dsphoenix.soundvault.utils.TAG
import com.dsphoenix.soundvault.utils.mediaplayer.MediaPlayer
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TrackDetailsFragment :
    ViewBindingFragment<TrackDetailsFragmentBinding>(TrackDetailsFragmentBinding::inflate) {
    private val viewModel: TrackDetailsViewModel by activityViewModels()

    private var audioUri: String? = null

    @Inject
    lateinit var mediaPlayer: MediaPlayer

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
                tvGenres.text = track.genres?.joinToString(", ") { it }
            }
            viewModel.imageRef.observe(viewLifecycleOwner) { ref ->
                ref?.let { ivTrackCover.loadImage(requireContext(), it) }
            }
            viewModel.audioRef.observe(viewLifecycleOwner) { ref ->
                ref?.let { audioUri = it }
            }
        }
        binding.ibPlayButton.setOnClickListener {
            Log.d(TAG, "image button click listener called. playing audio")
            playAudio()
        }

        mediaPlayer.isPlaying.observe(viewLifecycleOwner) {
            Log.d(TAG, "isPlaying toggled, calling listener with $it")
            if (it != null) {
                togglePlayButton(it)
            }
        }
    }

    private fun playAudio() {
        audioUri?.let {
            mediaPlayer.setTrackUrl(it)
            mediaPlayer.playTrack()
        }
    }

    private fun togglePlayButton(isPlaying: Boolean) {
        if (isPlaying) {
            setupPauseButton()
        } else {
            setupPlayButton()
        }
    }

    private fun setupPlayButton() {
        binding.apply {
            ibPlayButton.setImageResource(R.drawable.ic_play)
            ibPlayButton.setOnClickListener {
                Log.d(TAG, "listener called")
                mediaPlayer.resume()
            }
        }
    }

    private fun setupPauseButton() {
        binding.apply {
            ibPlayButton.setImageResource(R.drawable.ic_pause)
            ibPlayButton.setOnClickListener {
                Log.d(TAG, "listener called")
                mediaPlayer.pause()
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