package com.dsphoenix.soundvault.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.databinding.ActivityMainBinding
import com.dsphoenix.soundvault.ui.homescreen.HomeFragment
import com.dsphoenix.soundvault.ui.searchscreen.SearchFragment
import com.dsphoenix.soundvault.ui.trackdetails.TrackDetailsFragment
import com.dsphoenix.soundvault.ui.uploadscreen.CreateTrackFragment
import com.dsphoenix.soundvault.ui.userscreen.UserProfileFragment
import com.dsphoenix.soundvault.utils.TAG
import com.dsphoenix.soundvault.utils.mediaplayer.MediaPlayer
import com.dsphoenix.soundvault.utils.navigation.NavigationController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val BACKSTACK_ROOT_FRAGMENT_TAG = "root_fragment"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var mediaPlayer: MediaPlayer

    @Inject
    lateinit var navigationController: NavigationController

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigationController.initialize(supportFragmentManager, R.id.fragment_container_view)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            startSignInFlow()
        }

        setupView()
    }

    private fun setupView() {
        initializeNavigationBar()

        binding.apply {
            tvToolbarText.text = "Greetings"

            mediaPlayer.currentTrack.observe(this@MainActivity) { track ->
                Glide.with(this@MainActivity)
                    .load(track.imagePath)
                    .placeholder(R.drawable.ic_music_note)
                    .into(ivTrackCover)

                tvTrackName.text = getString(R.string.track_viewholder_title).format(track.authorName, track.name)
            }

            mediaPlayer.isPlaying.observe(this@MainActivity) { playing ->
                if (playing != null) {
                    togglePlayButton(playing)
                    if (playing && !isTrackDetailsScreenVisible()) {
                        mediaPlayer.trackProgress.observe(this@MainActivity) {
                            progressBar.progress = if (it >= 0) it else 0
                        }
                        layoutMediaPlayer.visibility = View.VISIBLE
                    }
                }
            }

            mediaPlayer.onCompletionCallback = ::hidePlayerLayout

            layoutMediaPlayer.setOnClickListener { navigateToTrackDetails(mediaPlayer.currentTrack.value?.id as String) }
        }
    }

    private fun playTrack(track: Track) {
        mediaPlayer.setTrackToPlay(track)
    }

    private fun togglePlayButton(isPlaying: Boolean) {
        if (isPlaying) {
            setupPauseButton()
        } else {
            setupPlayButton()
        }
    }

    private fun setupPlayButton() {
        binding.apply {
            ivPlayPause.setImageResource(R.drawable.ic_play)
            ivPlayPause.setOnClickListener {
                mediaPlayer.resume()
            }
        }
    }

    private fun setupPauseButton() {
        binding.apply {
            ivPlayPause.setImageResource(R.drawable.ic_pause)
            ivPlayPause.setOnClickListener {
                mediaPlayer.pause()
            }
        }
    }

    private fun hidePlayerLayout() {
        binding.layoutMediaPlayer.visibility = View.GONE
        mediaPlayer.trackProgress.removeObservers(this)
    }

    private fun showPlayerLayout() {
        binding.layoutMediaPlayer.visibility = View.VISIBLE
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
            R.id.profile_option -> {
                navigateToUserProfile()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeNavigationBar() {
        val navigationView: BottomNavigationView = findViewById(R.id.bottom_navigation_bar)
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
        } else {
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
            .addOnCompleteListener {
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
        navigationController.popBackStack()
        navigationController.replaceFragment(
            CreateTrackFragment(),
            backStackTag = BACKSTACK_ROOT_FRAGMENT_TAG
        )
    }

    private fun navigateToHomeScreen() {
        showToolbarAndNavBar()
        navigationController.popBackStack()
        val fragment = HomeFragment().apply {
            onTrackClickListener = ::playTrack
        }
        navigationController.replaceFragment(
            fragment,
            tag = HomeFragment.NAVIGATION_TAG,
            backStackTag = BACKSTACK_ROOT_FRAGMENT_TAG
        )
    }

    private fun navigateToSearchScreen() {
        showToolbarAndNavBar()
        navigationController.popBackStack()
        navigationController.replaceFragment(
            SearchFragment(),
            backStackTag = BACKSTACK_ROOT_FRAGMENT_TAG
        )
    }

    private fun navigateToUserProfile() {
        hideToolbarAndNavbar()
        navigationController.popBackStack()
        navigationController.replaceFragment(
            UserProfileFragment(),
            backStackTag = BACKSTACK_ROOT_FRAGMENT_TAG
        )
    }

    private fun navigateToTrackDetails(trackId: String) {
        hideToolbarAndNavbar()
        hidePlayerLayout()
        navigationController.popBackStack()
        navigationController.replaceFragment(
            TrackDetailsFragment.createInstance(trackId),
            tag = TrackDetailsFragment.NAVIGATION_TAG,
            backStackTag = BACKSTACK_ROOT_FRAGMENT_TAG
        )
    }

    private fun hideToolbarAndNavbar() {
        binding.toolbar.visibility = View.GONE
        binding.bottomNavigationBar.visibility = View.GONE
    }

    private fun showToolbarAndNavBar() {
        binding.toolbar.visibility = View.VISIBLE
        binding.bottomNavigationBar.visibility = View.VISIBLE
    }

    private fun isHomeScreenVisible(): Boolean {
        val homeFragment = navigationController.findFragmentByTag(HomeFragment.NAVIGATION_TAG)
        return homeFragment == null || !homeFragment.isVisible
    }

    private fun isTrackDetailsScreenVisible(): Boolean {
        val trackDetailsFragment = navigationController.findFragmentByTag(TrackDetailsFragment.NAVIGATION_TAG)
        return trackDetailsFragment != null && trackDetailsFragment.isVisible
    }

    override fun onBackPressed() {
        if (isTrackDetailsScreenVisible()) {
            showPlayerLayout()
        }

        if (navigationController.backStackEntryCount > 1) {
            navigationController.popBackStack()
        }
        else if (isHomeScreenVisible()) {
            binding.bottomNavigationBar.selectedItemId = R.id.navigation_home
            navigateToHomeScreen()
        } else {
            supportFinishAfterTransition()
        }
    }
}