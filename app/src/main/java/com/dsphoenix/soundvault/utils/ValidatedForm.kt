package com.dsphoenix.soundvault.utils

import kotlinx.coroutines.flow.Flow

interface ValidatedForm {
    val isValid: Flow<Boolean>
}