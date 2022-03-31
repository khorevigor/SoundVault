package com.dsphoenix.soundvault.utils.mediaplayer

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.utils.TAG
import kotlinx.coroutines.flow.MutableStateFlow

class MediaPlayer {
    private val _isPlaying = MutableStateFlow<Boolean?>(null)
    val isPlaying = _isPlaying.asLiveData()

    private val _currentTrack = MutableLiveData<Track>()
    val currentTrack: LiveData<Track> = _currentTrack

    var onCompletionCallback: (() -> Unit)? = null

    private val mediaPlayer = MediaPlayer().apply {
        setOnPreparedListener {
            start()
            _isPlaying.value = true
        }
        setOnCompletionListener {
            _isPlaying.value = false
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
        _isPlaying.value = false
    }

    fun resume() {
        Log.d(TAG, "resume called")
        mediaPlayer.start()
        _isPlaying.value = true
    }
}