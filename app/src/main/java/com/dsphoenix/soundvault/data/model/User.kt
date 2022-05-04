package com.dsphoenix.soundvault.data.model

import android.net.Uri

data class User(
    val uid: String? = null,
    val hasSubscription: Boolean? = null,
    val name: String? = null,
    val avatarUri: Uri? = null,
    val avatarPath: String? = null
)
