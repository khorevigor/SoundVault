package com.dsphoenix.soundvault.ui.homescreen

import androidx.lifecycle.*
import com.dsphoenix.soundvault.data.AudioRepository
import com.dsphoenix.soundvault.data.UserRepository
import com.dsphoenix.soundvault.data.model.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val uid: MutableStateFlow<String> = MutableStateFlow(FirebaseAuth.getInstance().currentUser?.uid.toString())

    private val _user = uid.flatMapConcat { flow { emit(userRepository.getUser(it)) } }
    val user: LiveData<User> = _user.asLiveData()

    var tracks = audioRepository.tracks.asLiveData()

    init {
        viewModelScope.launch {
            _user.collect {
                AudioRepository.isSubscriptionEnabled = it.hasSubscription ?: false
                audioRepository.fetchTracks()
            }
        }
    }

    fun toggleSubscription() {
        viewModelScope.launch {
            val updatedUser = user.value?.copy(hasSubscription = !user.value?.hasSubscription!!)
            if (updatedUser != null) {
                userRepository.updateUser(updatedUser)
            }
            audioRepository.refreshTracks()
        }
    }

    fun refreshTracks() {
        viewModelScope.launch {
            audioRepository.refreshTracks()
        }
    }

    fun fetchTracks() {
        viewModelScope.launch {
            audioRepository.fetchTracks()
        }
    }
}
