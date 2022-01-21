package com.dsphoenix.soundvault.ui.uploadscreen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.dsphoenix.soundvault.data.AudioRepository
import com.dsphoenix.soundvault.data.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "UploadFileViewModel"

@HiltViewModel
class UploadFileViewModel @Inject constructor(
    private val audioRepository: AudioRepository
)
    : ViewModel() {
    var filename = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val uri = MutableLiveData<Uri>()
    val isValid: LiveData<Boolean>

    init {
        filename = Transformations.map(uri) {uri.value?.lastPathSegment} as MutableLiveData<String>
        isValid = Transformations.switchMap(filename) {Transformations.map(uri) {uri.value != null && !filename.value.isNullOrEmpty()} }
    }

    fun uploadTrack(){
        Log.d(TAG, "filename is ${filename.value}, description is ${description.value}")
        Log.d(TAG, "uri is ${uri.value}")

        val track = Track(
            name = filename.value ?: "",
            description = description.value ?: "",
            uri = uri.value ?: Uri.EMPTY,
            remotePath = "audio/${filename.value}"
        )

        audioRepository.uploadTrack(track)
    }
}