package com.dsphoenix.soundvault.ui.uploadscreen

import androidx.lifecycle.*
import com.dsphoenix.soundvault.data.AudioRepository
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.ui.uploadscreen.forms.distributionplan.DistributionPlanForm
import com.dsphoenix.soundvault.ui.uploadscreen.forms.pickfile.FileForm
import com.dsphoenix.soundvault.ui.uploadscreen.forms.pickgenres.GenresForm
import com.dsphoenix.soundvault.ui.uploadscreen.forms.pickimage.ImageForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTrackViewModel @Inject constructor(
    private val audioRepository: AudioRepository
) : ViewModel() {
    val fileForm = FileForm()
    val imageForm = ImageForm()
    val genresForm = GenresForm()
    val distributionPlanForm = DistributionPlanForm()

    val isValid = combine(
        fileForm.isValid,
        imageForm.isValid,
        genresForm.isValid,
        distributionPlanForm.isValid
    ) {
        fileValid, imageValid, genresValid, planValid ->
        fileValid && imageValid && genresValid && planValid
    }

    fun uploadTrack() {
        val track = Track(
            name = fileForm.filename.value,
            authorName = fileForm.authorName.value,
            uri = fileForm.uri.value,
            imageUri = imageForm.uri.value,
            genres = genresForm.genres.value.keys.toList(),
            distributionPlan = distributionPlanForm.distributionPlan.value,
            distributionBundle = distributionPlanForm.distributionBundles.value.toList(),
            singlePrice = distributionPlanForm.singlePrice.value
        )

        viewModelScope.launch {
            audioRepository.uploadTrack(track)
        }
    }
}
