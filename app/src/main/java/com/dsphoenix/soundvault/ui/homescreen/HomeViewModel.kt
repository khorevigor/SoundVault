package com.dsphoenix.soundvault.ui.homescreen

import androidx.lifecycle.*
import com.dsphoenix.soundvault.data.AudioRepository
import com.dsphoenix.soundvault.data.UserRepository
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    audioRepository: AudioRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    val uid: MutableStateFlow<String> = MutableStateFlow("")

    private val _user: Flow<User> = uid.flatMapLatest {
        userRepository.getUser(it)
    }
    val user = _user.asLiveData()

    val tracks: LiveData<List<Track>> = _user.flatMapLatest {
        AudioRepository.isSubscriptionEnabled = it.hasSubscription ?: false
        audioRepository.getTracks()
    }.asLiveData()

    fun toggleSubscription() {
        viewModelScope.launch {
            val updatedUser = user.value?.copy(hasSubscription = !user.value?.hasSubscription!!)
            if (updatedUser != null) {
                userRepository.updateUser(updatedUser)
            }
        }
    }
}
