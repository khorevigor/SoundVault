package com.dsphoenix.soundvault.utils.navigation

import androidx.fragment.app.Fragment

interface NavigationController {
    fun addFragment(
        fragment: Fragment,
        tag: String? = null,
        addToBackStack: Boolean = true,
        backStackTag: String? = null
    )

    fun removeFragment(fragment: Fragment)

    fun replaceFragment(
        fragment: Fragment,
        tag: String? = null,
        addToBackStack: Boolean = true,
        backStackTag: String? = null
    )

    fun popBackStack()
    fun popBackStack(tag: String, flags: Int)
}