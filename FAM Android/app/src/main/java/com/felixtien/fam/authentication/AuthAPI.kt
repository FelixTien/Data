package com.felixtien.fam.authentication

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.felixtien.fam.home.HomeAPI
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.messaging

enum class AuthProvider(val value: String){
    EMAIL(value = "password"),
    PHONE(value = "phone"),
    GOOGLE(value = "google.com")
}
object AuthAPI{
    @SuppressLint("StaticFieldLeak")
    private val db = Firebase.firestore
    var signIn by mutableStateOf(Firebase.auth.currentUser != null)
    var progress by mutableStateOf(false)
    fun signIn(credential: AuthCredential, failure: (Boolean) -> Unit){
        Firebase.auth.signInWithCredential(credential)
            .addOnSuccessListener {
                attempt(attempts = 0, failure = failure)
            }
            .addOnFailureListener {
                failure(true)
            }
    }
    private fun attempt(attempts: Int, max: Int = 100, delay: Long = 1000, failure: (Boolean) -> Unit) {
        currentUserExist { exists ->
            if (exists) {
                val id = Firebase.auth.currentUser?.uid ?: return@currentUserExist
                subscribe(id = id){
                    Log.d("TAG","Subscribed to $id topic")
                }
                signIn = true
                HomeAPI.page.intValue = 0
                failure(false)
            } else if (attempts < max - 1) {
                Handler(Looper.getMainLooper()).postDelayed({
                    attempt(attempts = attempts + 1, failure = failure)
                }, delay)
            } else {
                failure(true)
            }
        }
    }
    fun reAuth(credential: AuthCredential, failure: (Boolean) -> Unit){
        val user = Firebase.auth.currentUser ?: return
        user.reauthenticate(credential)
            .addOnSuccessListener {
                signIn = false
                HomeAPI.problem.value = false
                HomeAPI.page.intValue = 0
                deleteAccount()
                failure(false)
            }
            .addOnFailureListener {
                failure(true)
            }
    }
    private fun currentUserExist(completion: (Boolean) -> Unit){
        val id = Firebase.auth.currentUser?.uid ?: ""
        db
            .collection("User")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()){
                    completion(true)
                }else {
                    completion(false)
                }
            }
            .addOnFailureListener {
                completion(false)
            }
    }
    fun signOut(){
        val id = Firebase.auth.currentUser?.uid ?: return
        unsubscribe(id = id){
            Log.d("TAG","Unsubscribed to $id topic")
        }
        Firebase.auth.signOut()
        progress = false
        signIn = false
    }
    fun deleteAccount(): Task<Void> {
        val user = Firebase.auth.currentUser ?: return Tasks.forException(FirebaseAuthException("no-current-user", "No user currently signed in"))
        return user.delete()
    }
    fun providersData(): List<AuthProvider>{
        val providerData = Firebase.auth.currentUser?.providerData ?: return listOf()
        val providers = mutableListOf<AuthProvider>()
        for (provider in providerData){
            val option = AuthProvider.entries.find { it.value == provider.providerId }
            if (option == null){
                Log.d("TAG", "Provider option not found: ${provider.providerId}")
            }else{
                providers.add(option)
            }
        }
        return providers
    }
    fun subscribe(id: String, completion: () -> Unit){
        Firebase.messaging.subscribeToTopic(id)
            .addOnSuccessListener {
                completion()
            }
    }
    fun unsubscribe(id: String, completion: () -> Unit){
        Firebase.messaging.unsubscribeFromTopic(id)
            .addOnSuccessListener {
                completion()
            }
    }
    private fun authEmail(mail: String, password: String, delete: Boolean, error: (EmailException?) -> Unit){
        if (mail.isEmpty() || password.isEmpty()){
            error(EmailException.EMPTY)
            return
        }
        Firebase.auth.signInWithEmailAndPassword(mail, password)
            .addOnSuccessListener {
                if (it.user?.isEmailVerified == true) {
                    if (delete){
                        deleteAccount()
                        error(null)
                    }else{
                        signIn = true
                    }
                } else {
                    error(EmailException.VERIFY)
                    Firebase.auth.signOut()
                }
            }
            .addOnFailureListener {
                error(EmailException.INVALID)
            }
    }
    fun signInEmail(mail: String, password: String, error: (EmailException?) -> Unit){
        authEmail(mail = mail, password = password, delete = false, error = error)
    }
    fun deleteEmail(mail: String, password: String, error: (EmailException?) -> Unit){
        authEmail(mail = mail, password = password, delete = true, error = error)
    }
    private fun authGoogle(google: Google, delete: Boolean){
        val signInIntent = google.signInIntent()
        if (delete) Google.deleteLauncher.launch(signInIntent) else Google.signInLauncher.launch(signInIntent)
        Google.gsc.signOut()
    }
    fun signInGoogle(google: Google){
        authGoogle(google = google, delete = false)
    }
    fun deleteGoogle(google: Google){
        authGoogle(google = google, delete = true)
    }
    private fun authPhone(smsCode: String, delete: Boolean, error: (PhoneException?) -> Unit){
        val credential = PhoneAuthProvider.getCredential(Phone.storedVerificationId, smsCode)
        if (delete){
            reAuth(credential = credential){ failure ->
                error(if (failure) PhoneException.INCORRECT else null)
            }
        }else{
            signIn(credential = credential){ failure ->
                error(if (failure) PhoneException.INCORRECT else null)
            }
        }
    }
    fun signInPhone(smsCode: String, error: (PhoneException?) -> Unit){
        authPhone(smsCode = smsCode, delete = false, error = error)
    }
    fun deletePhone(smsCode: String, error: (PhoneException?) -> Unit){
        authPhone(smsCode = smsCode, delete = true, error = error)
    }
}