package com.dsphoenix.soundvault.data.model

import android.net.Uri

data class Track (
    val name: String? = null,
    val description: String? = null,
    val uri: Uri? = null,
    val path: String? = null
    )