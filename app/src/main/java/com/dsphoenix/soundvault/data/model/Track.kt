package com.dsphoenix.soundvault.data.model

import android.net.Uri
import com.dsphoenix.soundvault.utils.constants.DistributionPlan

data class Track (
    val name: String? = null,
    val description: String? = null,
    val uri: Uri? = null,
    val path: String? = null,
    val distributionPlan: DistributionPlan? = null
    )