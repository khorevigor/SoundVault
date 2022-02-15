package com.dsphoenix.soundvault.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.ActivityMainBinding
import com.dsphoenix.soundvault.ui.homescreen.HomeFragment
import com.dsphoenix.soundvault.ui.searchscreen.SearchFragment
import com.dsphoenix.soundvault.ui.uploadscreen.UploadFileFragment
import com.dsphoenix.soundvault.ui.userscreen.UserProfileFragment
import com.dsphoenix.soundvault.utils.TAG
import com.dsphoenix.soundvault.utils.navigation.NavigationController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

private const val BACKSTACK_ROOT_FRAGMENT_TAG = "root_fragment"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationController {
    private lateinit var binding: ActivityMainBinding

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            startSignInFlow()
        }

        setupView()
        initializeNavigationBar()
    }

    private fun setupView() {
        binding.tvToolbarText.text = "Greetings"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_option -> {
                signOutActiveUser()
                true
            }
            R.id.profile_option-> {
                navigateToUserProfile()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeNavigationBar() {
        val navigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)
        navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> navigateToHomeScreen()
                R.id.navigation_search -> navigateToSearchScreen()
                R.id.navigation_library -> navigateToUploadScreen()
            }
            true
        }
        navigationView.setOnItemReselectedListener { }
    }

    private fun startSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse

        if (result.resultCode == RESULT_OK) {
            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
            Log.d(TAG, "User with UID $currentUserUid authenticated successfully")
            navigateToHomeScreen()
        }
        else {
            if (response == null) {
                Log.d(TAG, "User cancelled sign-in flow")
                moveTaskToBack(true)
                finishApplication()
            } else {
                Log.d(TAG, "Authentication error, ${response.error?.errorCode}")
            }
        }
    }

    private fun signOutActiveUser() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener{
                Log.d(TAG, "User signed out")
            }
        startSignInFlow()
    }

    private fun finishApplication() {
        moveTaskToBack(true)
        finish()
    }

    private fun navigateToUploadScreen() {
        showToolbarAndNavBar()
        popBackStack()
        replaceFragment(UploadFileFragment(), backStackTag = BACKSTACK_ROOT_FRAGMENT_TAG)
    }

    private fun navigateToHomeScreen() {
        showToolbarAndNavBar()
        popBackStack()
        replaceFragment(HomeFragment(), tag = HomeFragment.navigationTag, backStackTag = BACKSTACK_ROOT_FRAGMENT_TAG)
    }

    private fun navigateToSearchScreen() {
        showToolbarAndNavBar()
        popBackStack()
        replaceFragment(SearchFragment(), backStackTag = BACKSTACK_ROOT_FRAGMENT_TAG)
    }

    private fun navigateToUserProfile() {
        hideToolbarAndNavbar()
        popBackStack()
        replaceFragment(UserProfileFragment(), backStackTag = BACKSTACK_ROOT_FRAGMENT_TAG)
    }

    private fun hideToolbarAndNavbar() {
        binding.toolbar.visibility = View.GONE
        binding.bottomNavigationBar.visibility = View.GONE
    }

    private fun showToolbarAndNavBar() {
        binding.toolbar.visibility = View.VISIBLE
        binding.bottomNavigationBar.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        val homeFragment = supportFragmentManager.findFragmentByTag(HomeFragment.navigationTag)
        if (supportFragmentManager.backStackEntryCount > 1)
            popBackStack()
        else if (homeFragment == null || !homeFragment.isVisible) {
            navigateToHomeScreen()
        } else {
            supportFinishAfterTransition()
        }
    }

    // NavigationController
    override fun addFragment(
        fragment: Fragment,
        tag: String?,
        addToBackStack: Boolean,
        backStackTag: String?
    ) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view, fragment, tag)
            if (addToBackStack) {
                addToBackStack(backStackTag)
            }
        }
    }

    override fun removeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
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
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, fragment, tag)
            if (addToBackStack) {
                addToBackStack(backStackTag)
            }
        }
    }

    override fun popBackStack() {
        supportFragmentManager.popBackStackImmediate()
    }

    override fun popBackStack(tag: String, flags: Int) {
        supportFragmentManager.popBackStackImmediate(tag, flags)
    }
}