package com.dsphoenix.soundvault.ui.userscreen

import android.net.Uri
import androidx.lifecycle.*
import com.dsphoenix.soundvault.data.AudioRepository
import com.dsphoenix.soundvault.data.UserRepository
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.data.model.User
import com.dsphoenix.soundvault.utils.collectFlow
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

    private val _uid: MutableStateFlow<String> =
        MutableStateFlow(FirebaseAuth.getInstance().currentUser?.uid.toString())

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    private var isUserChanged = false

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks = _tracks.asStateFlow()

    init {
        collectFlow(_uid) { id ->
            _user.value = userRepository.getUser(id)
        }

        collectFlow(_user) { user ->
            AudioRepository.isSubscriptionEnabled = user.hasSubscription ?: false
            audioRepository.getTracks(mapOf(DbConstants.TRACK_UPLOADER_ID to user.uid.toString()))
                .collectLatest { tracks ->
                    _tracks.value = tracks
                }
        }
    }

    fun setUserName(name: String) {
        _user.value = _user.value.copy(name = name)
        isUserChanged = true
    }

    fun setAvatarUri(uri: Uri) {
        _user.value = _user.value.copy(avatarUri = uri)
        isUserChanged = true
    }

    fun saveChanges() {
        if (isUserChanged) {
            viewModelScope.launch {
                isUserChanged = false
                userRepository.updateUser(_user.value)
            }
        }
    }
}
