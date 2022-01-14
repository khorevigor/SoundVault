package com.dsphoenix.soundvault

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.dsphoenix.soundvault.ui.uploadscreen.UploadFileFragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var signOutButton: Button

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signOutButton = this.findViewById(R.id.sign_out_button)
        signOutButton.setOnClickListener { signOutActiveUser() }

        if (savedInstanceState == null)
            startSignInFlow()
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
        response?.let {
            if (result.resultCode == RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                Log.d(TAG, "User authenticated successfully, $user")
                navigateToUploadScreen()
            } else {
                Log.d(TAG, "Authentication error, ${response.error?.errorCode}")
            }
        } ?: Log.d(TAG, "User cancelled sign-in flow")
    }

    private fun signOutActiveUser() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener{
                Log.d(TAG, "User signed out")
            }
        startSignInFlow()
    }

    private fun navigateToUploadScreen() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<UploadFileFragment>(R.id.fragment_container_view)
        }
    }
}