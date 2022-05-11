package com.dsphoenix.soundvault.utils.mediaplayer

import android.media.MediaPlayer
import android.util.Log
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.utils.TAG
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.isActive
import java.util.*
import kotlin.math.roundToInt

class MediaPlayer {
    private val _isPlaying = MutableStateFlow<Boolean?>(null)
    val isPlaying = _isPlaying.asStateFlow()

    private val _isShuffled = MutableStateFlow(false)
    val isShuffled = _isShuffled.asStateFlow()

    private val _isLooping = MutableStateFlow(false)
    val isLooping = _isLooping.asStateFlow()

    private val _trackCurrentPosition = flow {
        while (currentCoroutineContext().isActive) {
            if (mediaPlayer.isPlaying) {
                emit(mediaPlayer.currentPosition)
            }
            delay(100)
        }
    }

    val trackCurrentPosition = _trackCurrentPosition

    private val _trackDuration = MutableStateFlow(0)
    val trackDuration = _trackDuration.asStateFlow()

    val trackProgress = _trackCurrentPosition.mapLatest {
        ((it.toDouble() / mediaPlayer.duration) * 100).roundToInt()
    }

    private var originalPlaybackList = emptyList<Track>()
    private var playbackList = emptyList<Track>()
    private var additionalQueue: Queue<Track> = LinkedList()

    private var currentIndex: Int = -1

    private val _currentTrack = MutableStateFlow(Track())
    val currentTrack = _currentTrack.asStateFlow()

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

    fun playTrack(track: Track) {
        setTrackToPlay(track)
        if (playbackList.isNotEmpty()) {
            updateCurrentIndex()
        }
    }

    fun setTracksQueue(tracks: List<Track>) {
        originalPlaybackList = tracks
        playbackList = tracks
    }

    fun addTrackToQueue(track: Track) {
        additionalQueue.add(track)
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
        val track = additionalQueue.poll()
        setTrackToPlay(track ?: next())
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

    private fun setTrackToPlay(track: Track) {
        _currentTrack.value = track
        _currentTrack.value?.path.let { path ->
            mediaPlayer.run {
                reset()
                setDataSource(path)
                prepareAsync()
            }
        }
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

    private fun updateCurrentIndex() {
        currentIndex = playbackList.indexOf(_currentTrack.value)
    }

    private fun next() = playbackList.getOrElse(++currentIndex) { getFirstOrCurrent() }
    private fun prev() = playbackList.getOrElse(--currentIndex) { getLastOrCurrent() }

    private fun nextFromQueue() = additionalQueue.poll()

}