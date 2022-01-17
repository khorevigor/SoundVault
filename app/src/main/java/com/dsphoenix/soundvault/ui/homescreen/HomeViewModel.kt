package com.dsphoenix.soundvault.ui.homescreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dsphoenix.soundvault.data.AudioRepository
import com.dsphoenix.soundvault.data.model.Track

class HomeViewModel(
    private val audioRepository: AudioRepository
) : ViewModel() {
    val tracks: LiveData<List<Track>> = audioRepository.getTracks()
}