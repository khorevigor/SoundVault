package com.dsphoenix.soundvault.ui.homescreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dsphoenix.soundvault.data.AudioRepository
import com.dsphoenix.soundvault.data.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    audioRepository: AudioRepository
) : ViewModel() {
    val tracks: LiveData<List<Track>> = audioRepository.getTracks().asLiveData()
}