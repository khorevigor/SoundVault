package com.dsphoenix.soundvault.ui.trackdetails

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.TrackDetailsFragmentBinding
import com.dsphoenix.soundvault.utils.TAG
import com.dsphoenix.soundvault.utils.collectLatestLifeCycleFlow
import com.dsphoenix.soundvault.utils.mediaplayer.MediaPlayer
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TrackDetailsFragment :
    ViewBindingFragment<TrackDetailsFragmentBinding>(TrackDetailsFragmentBinding::inflate) {
    private val viewModel: TrackDetailsViewModel by activityViewModels()

    @Inject
    lateinit var mediaPlayer: MediaPlayer

    var navigateBackButtonClickListener: (() -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val trackId = arguments?.getString(TRACK_ID_STRING_KEY)
        viewModel.setTrackId(trackId!!)
        setupView()
    }

    private fun setupView() {
        Log.d(TAG, "${binding.progressBar.min}, ${binding.progressBar.max}")
        binding.apply {
            collectLatestLifeCycleFlow(mediaPlayer.currentTrack) { track ->
                tvAuthorName.text = track.authorName
                tvTrackName.text = track.name
                tvGenres.text = track.genres?.joinToString(", ") { it }
                track.imagePath?.let { ivTrackCover.loadImage(requireContext(), it) }
            }

            collectLatestLifeCycleFlow(mediaPlayer.trackDuration) {
                tvDurationTotal.text = it.toFormattedTime()
            }

            collectLatestLifeCycleFlow(mediaPlayer.trackCurrentPosition) {
                tvDurationCurrent.text = it.toFormattedTime()
            }

            collectLatestLifeCycleFlow(mediaPlayer.trackProgress) {
                progressBar.progress = it
            }

            collectLatestLifeCycleFlow(mediaPlayer.isShuffled) {
                ibShuffleButton.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        if (it) R.color.rose else R.color.light_gray
                    )
                )
            }

            collectLatestLifeCycleFlow(mediaPlayer.isLooping) {
                ibLoopingButton.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        if (it) R.color.rose else R.color.light_gray
                    )
                )
            }

            ibPlayButton.setOnClickListener { playAudio() }

            ibShuffleButton.setOnClickListener { mediaPlayer.toggleShuffle() }

            ibPreviousButton.setOnClickListener { mediaPlayer.playPrev() }

            ibNextButton.setOnClickListener { mediaPlayer.playNext() }

            ibLoopingButton.setOnClickListener { mediaPlayer.toggleLooping() }

            backButton.setOnClickListener { navigateBackButtonClickListener?.invoke() }
        }

        collectLatestLifeCycleFlow(mediaPlayer.isPlaying) {
            if (it != null) {
                togglePlayButton(it)
            }
        }
    }

    private fun playAudio() {
        collectLatestLifeCycleFlow(viewModel.track) {
            mediaPlayer.playTrack(it)
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

    private fun Int.toFormattedTime(): String {
        val mins = this / 60000
        val secs = ((this % 60000).toDouble() / 1000).toInt()
        return context?.getString(
            R.string.time_mm_ss_format,
            mins,
            if (secs < 10) "0$secs" else secs
        ) ?: ""
    }

    // ???
    // Might be better to make Track parcelable and use it instead
    companion object {
        const val NAVIGATION_TAG = "TrackDetailsFragment"
        private const val TRACK_ID_STRING_KEY = "TRACK_ID"
        fun createInstance(trackId: String) = TrackDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(TRACK_ID_STRING_KEY, trackId)
            }
        }
    }
}