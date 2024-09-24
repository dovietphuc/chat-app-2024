package com.phucdv.chatapp.auth

import android.app.Activity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.local.ReferenceSet
import com.phucdv.chatapp.model.Conversation
import com.phucdv.chatapp.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.ArrayList
import java.util.Objects

fun registerUserByEmail(email: String, password: String) = callbackFlow<Task<AuthResult>> {
    var listener: ((Task<AuthResult>) -> Unit)? = { task ->
        trySend(task)
    }

    val firebaseAuth = FirebaseAuth.getInstance()
    firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if(task.isSuccessful) {
                task.result.user?.let {
                    pushNewUser(it)
                }
            }
            listener?.let { it(task) }
        }
    awaitClose {
        listener = null
    }
}

private fun pushNewUser(user: FirebaseUser) {
    val map = mapOf(
        "conversation" to emptyArray<Conversation>(),
        "created_time" to Timestamp.now(),
        "uuid" to user.uid,
        "email" to user.email
    )

    val firebaseFireStore = FirebaseFirestore.getInstance()
    firebaseFireStore.collection("user")
        .add(map)
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