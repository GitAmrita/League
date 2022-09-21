package com.example.league.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.league.model.Post
import com.example.league.model.User
import com.example.league.model.UserDetails
import com.example.league.network.NetworkResult
import com.example.league.repository.PostRepo
import com.example.league.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserDetailsViewModel(private val userRepo: UserRepo, private val postRepo: PostRepo): ViewModel() {

    val userDetailsObserver: LiveData<List<UserDetails>>
    get() = mutableUserDetailsLiveData
    private val mutableUserDetailsLiveData = MutableLiveData<List<UserDetails>> ()

    val userDetailsLiveDataErrorObserver: LiveData<String>
        get() = mutableUserDetailsErrorLiveData
    private val mutableUserDetailsErrorLiveData = MutableLiveData<String> ()

    var isSuccess: Boolean = true

    fun get() {
        var userLst = listOf<User>()
        var postLst = listOf<Post>()
        viewModelScope.launch(Dispatchers.Main) {
            val users = viewModelScope.async {
                userRepo.getUsers()
            }
            val posts = viewModelScope.async {
                postRepo.getPosts()
            }
            when(users.await()) {
                is NetworkResult.Success -> userLst = users.await().data!!
                else -> {
                    isSuccess = false
                    Log.e("USERS", users.await().message!! )
                }
            }
            when(posts.await()) {
                is NetworkResult.Success -> postLst = posts.await().data!!
                else -> {
                    isSuccess = false
                    Log.e("POSTS", posts.await().message!! )
                }
            }
            if (isSuccess)
                mutableUserDetailsLiveData.value = mapValues(userLst, postLst)
            else
                mutableUserDetailsErrorLiveData.value = " Please try again later"
        }
    }

    private fun mapValues(users: List<User>, posts: List<Post>): List<UserDetails> {
        val list = mutableListOf<UserDetails>()
        for (u in users) {
            for (p in posts) {
                if (p.userId == u.id) {
                    list.add(UserDetails(id = u.id, userName = u.userName, avatarUrl = u.avatarUrl,
                        title = p.title, body = p.body))
                }
            }
        }
        return list
    }
}
