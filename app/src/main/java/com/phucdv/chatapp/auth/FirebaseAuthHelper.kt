package com.phucdv.chatapp.auth

import android.app.Activity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

fun registerUserByEmail(email: String, password: String) = callbackFlow<Task<AuthResult>> {
    var listener: ((Task<AuthResult>) -> Unit)? = { task ->
        trySend(task)
    }

    val firebaseAuth = FirebaseAuth.getInstance()
    firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            listener?.let { it(task) }
        }
    awaitClose {
        listener = null
    }
}

fun login(email: String, password: String) = callbackFlow<Task<AuthResult>> {
    var listener: ((Task<AuthResult>) -> Unit)? = { task ->
        trySend(task)
    }

    val firebaseAuth = FirebaseAuth.getInstance()
    firebaseAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            listener?.let { it(task) }
        }
    awaitClose {
        listener = null
    }
}

fun changePassword(oldPassword: String, newPassword: String) = callbackFlow<Task<Void>> {
    var listener: ((Task<Void>) -> Unit)? = { task ->
        trySend(task)
    }
    val firebaseAuth = FirebaseAuth.getInstance()
    val user = firebaseAuth.currentUser ?: return@callbackFlow
    val email = user.email ?: return@callbackFlow
    val credential = EmailAuthProvider.getCredential(email, oldPassword)
    user.reauthenticate(credential).addOnCompleteListener { task ->
        if(task.isSuccessful) {
            user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                listener?.let { it(updateTask) }
            }
        } else {
            listener?.let { it(task) }
        }
    }
    awaitClose {
        listener = null
    }
}

fun forgotPassword(email: String) = callbackFlow<Task<Void>> {
    var listener: ((Task<Void>) -> Unit)? = {
        trySend(it)
    }
    val firebaseAuth = FirebaseAuth.getInstance()
    firebaseAuth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            listener?.let { it(task) }
        }
    awaitClose {
        listener = null
    }
}