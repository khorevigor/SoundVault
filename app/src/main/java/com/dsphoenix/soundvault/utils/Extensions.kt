package com.dsphoenix.soundvault.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

val Any.TAG: String
    get() {
        return if (!javaClass.isAnonymousClass) {
            javaClass.simpleName
        } else {
            javaClass.name
        }
    }

fun <T> ViewModel.collectFlow(flow: Flow<T>, collector: suspend (T) -> Unit) {
    viewModelScope.launch {
        flow.collect(collector)
    }
}

fun <T> Fragment.collectLatestLifeCycleFlow(flow: Flow<T>, collector: suspend (T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        flow.collectLatest(collector)
    }
}

fun <T> AppCompatActivity.collectLatestLifeCycleFlow(flow: Flow<T>, collector: suspend (T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        flow.collectLatest(collector)
    }
}
