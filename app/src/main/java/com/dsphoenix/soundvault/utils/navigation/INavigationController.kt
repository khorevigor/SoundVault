package com.dsphoenix.soundvault.utils.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface INavigationController {

    fun initialize(fragmentManager: FragmentManager, @IdRes containterViewId: Int)

    val backStackEntryCount: Int
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

    fun findFragmentByTag(tag: String): Fragment?
}