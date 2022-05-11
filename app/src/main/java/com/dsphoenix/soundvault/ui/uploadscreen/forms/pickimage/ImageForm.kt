package com.dsphoenix.soundvault.ui.uploadscreen.forms.pickimage

import android.net.Uri
import android.util.Log
import com.dsphoenix.soundvault.utils.ValidatedForm
import com.dsphoenix.soundvault.utils.TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest

class ImageForm: ValidatedForm {

    private val _uri = MutableStateFlow<Uri?>(null)
    val uri = _uri.asStateFlow()

    override val isValid = _uri.mapLatest { it != null }

    fun setUri(uri: Uri) {
        _uri.value = uri
    }
}