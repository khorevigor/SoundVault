package com.dsphoenix.soundvault.ui.uploadscreen.forms.pickimage

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dsphoenix.soundvault.utils.ValidatedForm
import com.dsphoenix.soundvault.utils.TAG

class ImageForm: ValidatedForm {
    private val _uri = MutableLiveData<Uri>()
    val uri: LiveData<Uri> = _uri

    private val _isValid = Transformations.map(_uri) { it != null }
    override val isValid: LiveData<Boolean> = _isValid

    fun setUri(uri: Uri) {
        Log.d(TAG, "Uri set $uri")
        _uri.value = uri
    }
}