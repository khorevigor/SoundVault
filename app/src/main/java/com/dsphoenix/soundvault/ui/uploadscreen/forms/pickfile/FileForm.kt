package com.dsphoenix.soundvault.ui.uploadscreen.forms.pickfile

import android.net.Uri
import com.dsphoenix.soundvault.utils.ValidatedForm
import kotlinx.coroutines.flow.*

class FileForm : ValidatedForm {
    private val _uri = MutableStateFlow<Uri?>(null)
    val uri = _uri.asStateFlow()

    private val _filename = MutableStateFlow("")
    val filename = _filename.asStateFlow()

    private val _authorName = MutableStateFlow("")
    val authorName = _authorName.asStateFlow()

    override val isValid: Flow<Boolean> = combine(
        _uri,
        _filename,
        _authorName
    ) { _, _, _ ->
        validate()
    }

    fun setFileName(filename: String) {
        _filename.value = filename
    }

    fun setUri(uri: Uri) {
        _uri.value = uri
    }

    fun setAuthorName(authorName: String) {
        _authorName.value = authorName
    }

    private fun validate() =
        _uri.value != null && _filename.value.isNotEmpty() && _authorName.value.isNotEmpty()
}