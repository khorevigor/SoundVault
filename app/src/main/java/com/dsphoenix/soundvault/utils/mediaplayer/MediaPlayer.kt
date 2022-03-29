package com.dsphoenix.soundvault.utils.mediaplayer

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.asLiveData
import com.dsphoenix.soundvault.utils.TAG
import kotlinx.coroutines.flow.MutableStateFlow

class MediaPlayer {
    private val _isPlaying = MutableStateFlow<Boolean?>(null)
    val isPlaying = _isPlaying.asLiveData()

    private val mediaPlayer = MediaPlayer().apply {
        setOnPreparedListener {
            start()
            _isPlaying.value = true
        }
        setOnCompletionListener {
            _isPlaying.value = false
        }
    }

    fun playTrack() {
        mediaPlayer.prepareAsync()
    }

    fun setTrackUrl(url: String) {
        mediaPlayer.run {
            reset()
            setDataSource(url)
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