package com.example.rma_carpoolin.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.rma_carpoolin.model.User

interface IUserRepository {
    fun save(user: User)
    fun update(user: User, picture: Uri)
    fun getAllUsers(): MutableLiveData<MutableList<User>>
    fun isRegistered(email: String, password:String)
    fun getUserById(userId: String): User?
}