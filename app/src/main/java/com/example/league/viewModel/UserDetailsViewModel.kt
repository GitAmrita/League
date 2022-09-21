package com.example.league.viewModel

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

/*
To capture the view, I have used two apis /users (username, avatar url) and /posts(title, body).
Since the teo apis are independent of each other, I have called them in parallel for fastest
retrieval of data.
fun mapValues creates the final view containing name, avatar, title, body from the responses of
the two apis.
*****************************Handling Error in Api:************************
The user is agnostic of how many api calls were made to render the data. So I didn't
 render the errors coming from the apis. If any of the apis return error, then we can't render the
 view. Hence, I show a generic error message as toast if either of them fail. We should log the
 specific errors for debugging in production env.

 An alternate design could be to show the data for the api that returns success and hide the fields
 for the api that return error. However, in this scenario, it doesn't add much value to the users to
 see partial view, so I didn't go for this design.

* */

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
                }
            }
            when(posts.await()) {
                is NetworkResult.Success -> postLst = posts.await().data!!
                else -> {
                    isSuccess = false
                }
            }
            if (isSuccess)
                mutableUserDetailsLiveData.value = mapValues(userLst, postLst)
            else
                mutableUserDetailsErrorLiveData.value = " Unable to fetch results, try again later."
        }
    }

    fun mapValues(users: List<User>, posts: List<Post>): List<UserDetails> {
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
