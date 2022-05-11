package com.dsphoenix.soundvault.ui.uploadscreen.forms.pickgenres

import com.dsphoenix.soundvault.utils.ValidatedForm
import kotlinx.coroutines.flow.*

class GenresForm: ValidatedForm {

    private val genreToColor = mutableMapOf<String, Int>()
    private val _genres = MutableStateFlow(mapOf<String, Int>())
    val genres = _genres.asStateFlow()

    override val isValid: Flow<Boolean> = _genres.mapLatest {
        it.isNotEmpty()
    }

    fun addGenre(genre: String, color: Int) {
        genreToColor[genre] = color
        _genres.value = genreToColor
    }

    fun removeGenre(genre: String) {
        genreToColor.remove(genre)
        _genres.value = genreToColor
    }
}