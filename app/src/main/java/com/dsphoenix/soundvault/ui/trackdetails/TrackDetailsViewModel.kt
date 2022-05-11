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
    val track = _trackId.flatMapLatest { audioRepository.getTrack(it) }

    fun setTrackId(id: String) {
        _trackId.value = id
    }
}