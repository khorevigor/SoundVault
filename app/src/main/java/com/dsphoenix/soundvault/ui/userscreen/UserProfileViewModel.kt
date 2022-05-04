package com.dsphoenix.soundvault.ui.userscreen

import android.net.Uri
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

    private val uid: MutableStateFlow<String> =
        MutableStateFlow(FirebaseAuth.getInstance().currentUser?.uid.toString())

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private var isUserChanged = false

    val tracks = audioRepository.tracks.asLiveData()

    init {
        viewModelScope.launch {
            uid.collect {
                val user = userRepository.getUser(it)
                _user.postValue(user)
                AudioRepository.isSubscriptionEnabled = user.hasSubscription ?: false
                audioRepository.fetchTracks(mapOf(DbConstants.TRACK_UPLOADER_ID to it))
            }
        }
    }

    fun setUserName(name: String) {
        _user.value = _user.value?.copy(name = name)
        isUserChanged = true
    }

    fun setAvatarUri(uri: Uri) {
        _user.value = _user.value?.copy(avatarUri = uri)
        isUserChanged = true
    }

    fun saveChanges() {
        if (isUserChanged) {
            _user.value?.let {
                viewModelScope.launch {
                    isUserChanged = false
                    userRepository.updateUser(it)
                }
            }
        }
    }
}
