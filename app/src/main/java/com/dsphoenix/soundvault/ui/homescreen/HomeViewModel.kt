package com.dsphoenix.soundvault.ui.homescreen

import androidx.lifecycle.*
import com.dsphoenix.soundvault.data.AudioRepository
import com.dsphoenix.soundvault.data.UserRepository
import com.dsphoenix.soundvault.data.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    audioRepository: AudioRepository,
    userRepository: UserRepository,
) : ViewModel() {

    val uid: MutableStateFlow<String> = MutableStateFlow("")

    val tracks: LiveData<List<Track>> = uid.flatMapLatest {
        userRepository.getUser(it)
    }.flatMapLatest {
        AudioRepository.isSubscriptionEnabled = it.hasSubscription ?: false
        audioRepository.getTracks()
    }.asLiveData()
}
