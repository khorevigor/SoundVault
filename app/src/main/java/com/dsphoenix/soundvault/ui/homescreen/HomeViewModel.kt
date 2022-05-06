package com.dsphoenix.soundvault.ui.homescreen

import androidx.lifecycle.*
import com.dsphoenix.soundvault.data.AudioRepository
import com.dsphoenix.soundvault.data.UserRepository
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.data.model.User
import com.dsphoenix.soundvault.utils.collectFlow
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

    private val _uid = MutableStateFlow(FirebaseAuth.getInstance().currentUser?.uid.toString())

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks = _tracks.asStateFlow()

    init {
        collectFlow(_uid) { id ->
            _user.value = userRepository.getUser(id)
        }

        collectFlow(_user) { user ->
            AudioRepository.isSubscriptionEnabled = user.hasSubscription ?: false
            audioRepository.getTracks().collectLatest { tracks ->
                _tracks.value = tracks
            }
        }
    }

    fun toggleSubscription() {
        viewModelScope.launch {
            val updatedUser = _user.value.copy(hasSubscription = !_user.value.hasSubscription!!)
            userRepository.updateUser(updatedUser)
            _user.value = updatedUser
        }
    }

    fun refreshTracks(onCompletionListener: (() -> Unit)? = null) {
        viewModelScope.launch {
            audioRepository.getTracks(skipCache = true).collectLatest { tracks ->
                _tracks.value = tracks
                onCompletionListener?.invoke()
            }
        }
    }
}
