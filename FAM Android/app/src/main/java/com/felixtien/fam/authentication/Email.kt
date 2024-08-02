package com.felixtien.fam.authentication

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

enum class EmailException {
    INVALID,
    LENGTH,
    PASSWORD,
    EXIST,
    VERIFY,
    SEVER,
    EMPTY,
    AUTH
}
class Email {
    private val db = Firebase.firestore
    fun signUp(mail: String, password: String, confirm: String, error: (EmailException?) -> Unit) {
        if (mail.isEmpty() || password.isEmpty()) {
            error(EmailException.EMPTY)
            return
        }
        if (!mail.contains('@')) {
            error(EmailException.INVALID)
            return
        }
        if (password != confirm) {
            error(EmailException.PASSWORD)
            return
        }
        Firebase.auth.createUserWithEmailAndPassword(mail, password)
            .addOnSuccessListener { result ->
                val user = result.user
                user?.sendEmailVerification()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Firebase.auth.signOut()
                        error(null)
                    } else {
                        error(EmailException.SEVER)
                    }
                }
            }
            .addOnFailureListener {
                if (it is FirebaseAuthInvalidCredentialsException) {
                    error(EmailException.LENGTH)
                } else {
                    error(EmailException.EXIST)
                }
            }
    }
    fun resetPassword(mail: String) {
        Firebase.auth.sendPasswordResetEmail(mail)
    }
    fun updatePassword(password: String, confirm: String, error: (EmailException?) -> Unit) {
        if (password.isEmpty() || confirm.isEmpty()) {
            error(EmailException.EMPTY)
            return
        }
        if (password != confirm) {
            error(EmailException.PASSWORD)
            return
        }
        val user = Firebase.auth.currentUser ?: return
        user.updatePassword(password)
            .addOnSuccessListener {
                error(null)
            }
            .addOnFailureListener {
                if (it is FirebaseAuthInvalidCredentialsException) {
                    error(EmailException.LENGTH)
                } else {
                    error(EmailException.AUTH)
                }
            }
    }

    fun updateEmail(mail: String, error: (EmailException?) -> Unit) {
        if (mail.isEmpty()) {
            error(EmailException.EMPTY)
            return
        }
        if (!mail.contains('@')) {
            error(EmailException.INVALID)
            return
        }
        db
            .collection("User")
            .whereEqualTo("email", mail)
            .get()
            .addOnSuccessListener { query ->
                if (query.isEmpty) {
                    val user = Firebase.auth.currentUser ?: return@addOnSuccessListener
                    user.verifyBeforeUpdateEmail(mail)
                        .addOnSuccessListener {
                            user.sendEmailVerification()
                                .addOnSuccessListener {
                                    error(null)
                                }
                        }
                        .addOnFailureListener {
                            error(EmailException.AUTH)
                        }
                } else {
                    error(EmailException.EXIST)
                }
            }
    }
}