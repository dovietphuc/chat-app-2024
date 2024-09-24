package com.phucdv.chatapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.phucdv.chatapp.model.Conversation
import com.phucdv.chatapp.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.ArrayList

class UserViewModel : ViewModel() {

    private val _allUser = MutableStateFlow<List<User>>(emptyList())
    val allUser = _allUser.asStateFlow()

    init {
        getAllUsers()
    }

    fun myUserId() : String {
        return _allUser.value.firstOrNull { user ->
            val fu = FirebaseAuth.getInstance().currentUser ?: return@firstOrNull false
            return@firstOrNull fu.uid == user.uuid
        }?.id ?: ""
    }

    fun getAllUsers() {
        val firebaseFireStore = FirebaseFirestore.getInstance()
        firebaseFireStore.collection("user")
            .addSnapshotListener { value, error ->
                val allUser = ArrayList<User>()
                value?.documents?.forEach { documentSnapshot ->
                    val id = documentSnapshot.id
                    val uuid = documentSnapshot.getString("uuid") ?: return@forEach
                    val email = documentSnapshot.getString("email") ?: return@forEach
                    val createTime = documentSnapshot.getTimestamp("created_time") ?: return@forEach
                    val user = User(id, uuid, email, createTime)
                    allUser.add(user)
                }
                _allUser.value = allUser
            }
    }

}