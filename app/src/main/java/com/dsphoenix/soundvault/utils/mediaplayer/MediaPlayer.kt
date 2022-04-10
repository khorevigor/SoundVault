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
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.math.roundToInt

class MediaPlayer {
    private val _isPlaying = MutableStateFlow<Boolean?>(null)
    val isPlaying = _isPlaying.asLiveData()

    private val _trackCurrentPosition = flow {
        while (currentCoroutineContext().isActive) {
            emit (mediaPlayer.currentPosition)
            delay(100)
        }
    }
    val trackCurrentPosition = _trackCurrentPosition.asLiveData()

    val trackProgress = _trackCurrentPosition.asLiveData().map {
        ((it.toDouble() / mediaPlayer.duration) * 100).roundToInt()
    }

    val duration
        get() = mediaPlayer.duration

    private val _currentTrack = MutableLiveData<Track>()
    val currentTrack: LiveData<Track> = _currentTrack

    var onCompletionCallback: (() -> Unit)? = null

    private val mediaPlayer = MediaPlayer().apply {
        setOnPreparedListener {
            start()
            _isPlaying.value = it.isPlaying
        }
        setOnCompletionListener {
            _isPlaying.value = it.isPlaying
            onCompletionCallback?.invoke()
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
}