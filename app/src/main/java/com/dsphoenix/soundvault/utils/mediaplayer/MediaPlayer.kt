package com.dsphoenix.soundvault.utils.mediaplayer

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.utils.TAG
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.math.roundToInt

class MediaPlayer {
    private val _isPlaying = MutableStateFlow<Boolean?>(null)
    val isPlaying = _isPlaying.asLiveData()

    private val _isShuffled = MutableStateFlow(false)
    val isShuffled = _isShuffled.asLiveData()

    private val _isLooping = MutableStateFlow(false)
    val isLooping = _isLooping.asLiveData()

    private val _trackCurrentPosition = flow {
        while (currentCoroutineContext().isActive) {
            if (mediaPlayer.isPlaying) {
                emit(mediaPlayer.currentPosition)
            }
            delay(100)
        }
    }

    val trackCurrentPosition = _trackCurrentPosition.asLiveData()

    private val _trackDuration = MutableLiveData<Int>()
    val trackDuration: LiveData<Int> = _trackDuration

    val trackProgress = _trackCurrentPosition.asLiveData().map {
        ((it.toDouble() / mediaPlayer.duration) * 100).roundToInt()
    }

    private var originalPlaybackList = emptyList<Track>()
    private var playbackList = emptyList<Track>()

    private var currentIndex: Int = -1

    private val _currentTrack = MutableLiveData<Track>()
    val currentTrack: LiveData<Track> = _currentTrack

    var onCompletionCallback: (() -> Unit)? = null

    private val mediaPlayer = MediaPlayer().apply {
        setOnPreparedListener {
            start()
            _trackDuration.value = this.duration
            _isPlaying.value = it.isPlaying
        }
        setOnCompletionListener {
            Log.d(TAG, "onCompletionListenerCalled")
            _isPlaying.value = it.isPlaying
            playNext()
        }
    }

    fun setTrackToPlay(track: Track) {
        _currentTrack.value = track
        _currentTrack.value?.path.let { path ->
            mediaPlayer.run {
                reset()
                setDataSource(path)
                prepareAsync()
            }
        }
        if (playbackList.isNotEmpty()) {
            updateCurrentIndex()
        }
    }

    fun setTracksQueue(tracks: List<Track>) {
        originalPlaybackList = tracks
        playbackList = tracks
    }

    private fun updateCurrentIndex() {
        currentIndex = playbackList.indexOf(_currentTrack.value)
    }

    fun pause() {
        Log.d(TAG, "pause called")
        mediaPlayer.pause()
        _isPlaying.value = mediaPlayer.isPlaying
    }

    fun resume() {
        Log.d(TAG, "resume called")
        mediaPlayer.start()
        _isPlaying.value = mediaPlayer.isPlaying
    }

    fun playNext() {
        mediaPlayer.stop()
        setTrackToPlay(next())
    }

    fun playPrev() {
        if (mediaPlayer.currentPosition < 2000) {
            setTrackToPlay(prev())
        }
        else {
            playCurrentFromStart()
        }
    }

    fun toggleShuffle() {
        if (_isShuffled.value) {
            playbackList = originalPlaybackList
            updateCurrentIndex()
        }
        else {
            playbackList = playbackList.shuffled()
            updateCurrentIndex()
        }
        _isShuffled.apply { value = !value }
    }

    fun toggleLooping() {
        mediaPlayer.isLooping = !mediaPlayer.isLooping
        _isLooping.value = mediaPlayer.isLooping
    }

    private fun playCurrentFromStart() {
        mediaPlayer.seekTo(0)
    }

    private fun getFirstOrCurrent(): Track {
        val track = _currentTrack.value
        return if (playbackList.isEmpty() && track != null) {
            currentIndex = -1
            track
        } else {
            currentIndex = 0
            playbackList.first()
        }
    }

    private fun getLastOrCurrent(): Track {
        val track = _currentTrack.value
        return if (playbackList.isEmpty() && track != null) {
            currentIndex = -1
            track
        } else {
            currentIndex = playbackList.lastIndex
            playbackList.last()
        }
    }

    private fun next(): Track {
        return playbackList.getOrElse(++currentIndex) { getFirstOrCurrent() }
    }

    private fun prev(): Track {
        return playbackList.getOrElse(--currentIndex) { getLastOrCurrent() }
    }
}