package com.dsphoenix.soundvault.ui.uploadscreen

import androidx.lifecycle.*
import com.dsphoenix.soundvault.data.AudioRepository
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.ui.uploadscreen.forms.distributionplan.DistributionPlanForm
import com.dsphoenix.soundvault.ui.uploadscreen.forms.pickfile.FileForm
import com.dsphoenix.soundvault.ui.uploadscreen.forms.pickgenres.GenresForm
import com.dsphoenix.soundvault.ui.uploadscreen.forms.pickimage.ImageForm
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val forms = listOf(fileForm, imageForm, genresForm, distributionPlanForm)

    val isValid = MediatorLiveData<Boolean>()

    init {
        forms.map {
            isValid.addSource(it.isValid) { isValid.value = validate() }
        }
    }

    fun uploadTrack() {
        val track = Track(
            name = fileForm.filename.value,
            authorName = fileForm.authorName.value,
            uri = fileForm.uri.value,
            path = "audio/${fileForm.filename.value}",
            imageUri = imageForm.uri.value,
            imagePath = "image/${imageForm.uri.value?.lastPathSegment}",
            genres = genresForm.genres.value?.keys?.toList(),
            distributionPlan = distributionPlanForm.distributionPlan.value,
            distributionBundle = distributionPlanForm.distributionBundle.value?.toList(),
            singlePrice = distributionPlanForm.singlePrice.value
        )

        viewModelScope.launch {
            audioRepository.uploadTrack(track)
        }
    }

    private fun validate() = forms.all { it.isValid.value == true }
}
