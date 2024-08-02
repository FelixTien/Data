package com.felixtien.fam.authentication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.felixtien.fam.R
import com.felixtien.fam.home.HomeAPI
import com.felixtien.fam.home.profile.graph.ProfileScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class Google(
    private val component: ComponentActivity
) {
    companion object{
        lateinit var signInLauncher: ActivityResultLauncher<Intent>
        lateinit var deleteLauncher: ActivityResultLauncher<Intent>
        @SuppressLint("StaticFieldLeak")
        lateinit var gsc: GoogleSignInClient
    }
    private val gso: GoogleSignInOptions
    var errorMatch = ""
    init {
        signInLauncher = handleActivityResult(delete = false)
        deleteLauncher = handleActivityResult(delete = true)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(component.getString(R.string.web_client_id))
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(component, gso)
    }
    private fun handleActivityResult(delete: Boolean): ActivityResultLauncher<Intent> {
        return component.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    val account = task.getResult(ApiException::class.java)
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    AuthAPI.progress = true
                    if (delete){
                        AuthAPI.reAuth(credential = credential){ failure ->
                            if (failure){
                                errorMatch = "Account not match."
                                AuthAPI.progress = false
                            }else{
                                HomeAPI.profileController.navigate(ProfileScreen.SuccessDelete.route)
                            }
                        }
                    }else {
                        AuthAPI.signIn(credential = credential) { }
                    }
                } catch (e: ApiException) {
                    Log.d("SignIn", "Sign in failed: ${e.statusCode}")
                }
            }
        }
    }
    fun signInIntent(): Intent {
        return gsc.signInIntent
    }
}