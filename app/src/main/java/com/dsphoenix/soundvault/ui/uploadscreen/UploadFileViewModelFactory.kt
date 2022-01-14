package com.dsphoenix.soundvault.ui.uploadscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dsphoenix.soundvault.data.AudioRepository
import com.dsphoenix.soundvault.utils.firebase.FirebaseStorageService
import com.dsphoenix.soundvault.utils.firebase.FirestoreService
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class UploadFileViewModelFactory(
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UploadFileViewModel::class.java)) {
            return UploadFileViewModel(AudioRepository(FirestoreService(), FirebaseStorageService)) as T
        } else throw IllegalArgumentException("Unknown ViewModel class")
    }
}
