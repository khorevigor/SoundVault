package com.dsphoenix.soundvault.utils.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import javax.inject.Inject

class NavigationController @Inject constructor()
    : INavigationController {

    private lateinit var fragmentManager: FragmentManager
    private var containerViewId: Int = 0

    override fun initialize(fragmentManager: FragmentManager, @IdRes containerViewId: Int) {
        this.fragmentManager = fragmentManager
        this.containerViewId = containerViewId
    }

    override val backStackEntryCount: Int
        get() = fragmentManager.backStackEntryCount

    override fun addFragment(
        fragment: Fragment,
        tag: String?,
        addToBackStack: Boolean,
        backStackTag: String?
    ) {
        fragmentManager.commit {
            setReorderingAllowed(true)
            add(containerViewId, fragment, tag)
            if (addToBackStack) {
                addToBackStack(backStackTag)
            }
        }
    }

    override fun removeFragment(fragment: Fragment) {
        fragmentManager.commit {
            setReorderingAllowed(true)
            remove(fragment)
        }
    }

    override fun replaceFragment(
        fragment: Fragment,
        tag: String?,
        addToBackStack: Boolean,
        backStackTag: String?
    ) {
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace(containerViewId, fragment, tag)
            if (addToBackStack) {
                addToBackStack(backStackTag)
            }
        }
    }

    override fun popBackStack() {
        fragmentManager.popBackStackImmediate()
    }

    override fun popBackStack(tag: String, flags: Int) {
        fragmentManager.popBackStackImmediate(tag, flags)
    }

    override fun findFragmentByTag(tag: String): Fragment? = fragmentManager.findFragmentByTag(tag)
}