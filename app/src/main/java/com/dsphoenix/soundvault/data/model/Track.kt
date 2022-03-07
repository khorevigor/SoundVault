package com.dsphoenix.soundvault.data.model

import android.net.Uri
import com.dsphoenix.soundvault.utils.constants.DistributionBundle
import com.dsphoenix.soundvault.utils.constants.DistributionPlan

data class Track(
    val name: String? = null,
    val authorName: String? = null,
    val description: String? = null,
    val uri: Uri? = null,
    val path: String? = null,
    val imageUri: Uri? = null,
    val imagePath: String? = null,
    val genres: List<String>? = null,
    val distributionPlan: DistributionPlan? = null,
    val distributionBundle: List<DistributionBundle>? = null,
    val singlePrice: String? = null
)