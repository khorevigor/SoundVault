package com.dsphoenix.soundvault.ui.uploadscreen.forms.pickgenres

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dsphoenix.soundvault.utils.ValidatedForm

class GenresForm: ValidatedForm {
    private val genreToColor = mutableMapOf<String, Int>()
    private val _genres = MutableLiveData<Map<String, Int>>()
    val genres: LiveData<Map<String, Int>> = _genres

    private val _isValid = Transformations.map(_genres) { it.isNotEmpty() }
    override val isValid: LiveData<Boolean> = _isValid

    fun addGenre(genre: String, color: Int) {
        genreToColor[genre] = color
        _genres.value = genreToColor
    }

    fun removeGenre(genre: String) {
        genreToColor.remove(genre)
        _genres.value = genreToColor
    }
}