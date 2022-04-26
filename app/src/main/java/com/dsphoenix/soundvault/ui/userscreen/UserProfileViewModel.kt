package com.dsphoenix.soundvault.ui.userscreen

import androidx.lifecycle.*
import com.dsphoenix.soundvault.data.AudioRepository
import com.dsphoenix.soundvault.data.UserRepository
import com.dsphoenix.soundvault.data.model.User
import com.dsphoenix.soundvault.utils.constants.DbConstants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    audioRepository: AudioRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val uid: MutableStateFlow<String> = MutableStateFlow(FirebaseAuth.getInstance().currentUser?.uid.toString())

    private val _user: Flow<User> = uid.flatMapLatest {
        flow { userRepository.getUser(it) }
    }
    val user = _user.asLiveData()

    val tracks = audioRepository.tracks.asLiveData()

    init {
        viewModelScope.launch {
            _user.collect {
                AudioRepository.isSubscriptionEnabled = it.hasSubscription ?: false
                audioRepository.fetchTracks(mapOf(DbConstants.TRACK_UPLOADER_ID to uid.value))
            }
        }
    }
}
