package com.dsphoenix.soundvault.utils

import androidx.lifecycle.LiveData

interface ValidatedForm {
    val isValid: LiveData<Boolean>
}