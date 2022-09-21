package com.example.league.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.league.repository.PostRepo
import com.example.league.repository.UserRepo

class UserDetailsViewModelFactory(private val userRepo: UserRepo,
                                  private val postRepo: PostRepo): ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            return UserDetailsViewModel(userRepo, postRepo) as T
        }
}