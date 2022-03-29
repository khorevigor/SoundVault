package com.dsphoenix.soundvault.ui.trackdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dsphoenix.soundvault.data.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TrackDetailsViewModel @Inject constructor(
    private val audioRepository: AudioRepository
) : ViewModel() {
    private val _trackId = MutableStateFlow("")
    private val _track = _trackId.flatMapLatest { audioRepository.getTrack(it) }

    val track = _track.asLiveData()
    val imageRef = _track.flatMapLatest { audioRepository.getTrackImageRef(it) }.asLiveData()

    val audioRef = _track.flatMapLatest { audioRepository.getTrackAudioRef(it) }.asLiveData()

    fun setTrackId(id: String) {
        _trackId.value = id
    }
}