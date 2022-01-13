package com.dsphoenix.soundvault.data.model

import android.net.Uri

data class Track (
    val name: String,
    val description: String?,
    val uri: Uri,
    val remotePath: String? = ""
    )