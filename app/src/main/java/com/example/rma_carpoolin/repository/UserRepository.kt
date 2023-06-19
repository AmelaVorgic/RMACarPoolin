package com.example.rma_carpoolin.repository

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.rma_carpoolin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class UserRepository :IUserRepository {
    private var firebaseAuth: FirebaseAuth = Firebase.auth
    private var firebaseStore: FirebaseFirestore = Firebase.firestore
    private var storageReference = FirebaseStorage.getInstance().reference
    private var collectionReference: CollectionReference = firebaseStore.collection("users")

    private var liveData: MutableLiveData<MutableList<User>> = MutableLiveData<MutableList<User>>()

    override fun save(user: User) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = firebaseAuth.currentUser?.uid
                    if (userId != null) {
                        val documentReference = collectionReference.document(userId)
                        user.id = userId
                        documentReference.set(user)
                            .addOnSuccessListener {
                                Log.d(ContentValues.TAG, "onSuccess: user profile created for ${user.email}")
                            }
                            .addOnFailureListener { exception ->
                                Log.d(ContentValues.TAG, "onFailure: ${exception.message}")
                            }
                    }
                }
            }
    }

    override fun update(user: User, picture: Uri) {
        val imageReference = storageReference.child("images/profileImages/${picture.lastPathSegment}")
        imageReference.putFile(picture)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Image uploaded $imageReference")
                imageReference.downloadUrl.addOnSuccessListener {
                        downloadUrl ->
                    collectionReference.get().addOnSuccessListener {
                            users ->
                        for (usero in users){
                            if(usero.id == user.id){
                                collectionReference.document(usero.id).update("picture", downloadUrl)
                                    .addOnSuccessListener {
                                        Log.d(ContentValues.TAG, "onSucess: Icon succesfully uploaded")
                                    }
                                    .addOnFailureListener {
                                        Log.d(ContentValues.TAG, "onFailure: ${it.message}")
                                    }

                            }
                        }
                    }
                    val users = liveData.value?.iterator()
                    users?.forEach {
                        if(it.id == user.id){
                            it.picture = downloadUrl.toString()
                        }
                    }
                }
            }
            .addOnFailureListener{
                fail ->
                Log.d(ContentValues.TAG, "Failed with exception", fail.cause);
            }
    }


    override fun getAllUsers(): MutableLiveData<MutableList<User>> {
        collectionReference.get().addOnSuccessListener { snapshots ->
            if(snapshots != null && !snapshots.isEmpty){
                val users = mutableListOf<User>()
                for(user in snapshots){
                    users.add(User(
                        user.data.get("id") as String,
                        user.data.get("email") as String,
                        user.data.get("password") as String,
                        user.data.get("picture") as String
                    ))
                }
                liveData.postValue(users)
            }else{
                Log.d(ContentValues.TAG, "No data")
            }
        }
        return liveData
    }

    override fun isRegistered(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                    task ->
                task.isSuccessful

            }
    }

    override fun getUserById(userId: String): User? {
        val users = liveData.value?.iterator()
        users?.forEach {
            if(it.id == userId){
                return it
            }

        }
        return null
    }


}