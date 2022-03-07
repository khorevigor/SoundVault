package com.dsphoenix.soundvault.ui.uploadscreen.forms.pickfile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dsphoenix.soundvault.utils.ValidatedForm
import java.io.File

class FileForm : ValidatedForm {
    private val _uri = MutableLiveData<Uri>()
    val uri: LiveData<Uri> = _uri

    private val _filename = Transformations.map(_uri) { uri -> getFileName(uri) } as MutableLiveData<String>
    val filename: LiveData<String> = _filename

    private val _authorName = MutableLiveData<String>()
    val authorName: LiveData<String> = _authorName

    override val isValid = MediatorLiveData<Boolean>()

    init {
        isValid.addSource(_uri) { isValid.value = validate() }
        isValid.addSource(_filename) { isValid.value = validate() }
        isValid.addSource(_authorName) { isValid.value = validate() }
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

    private fun validate() = _uri.value != null && !_filename.value.isNullOrEmpty() && !_authorName.value.isNullOrEmpty()

    private fun getFileName(uri: Uri) = File(uri.path!!).nameWithoutExtension
}