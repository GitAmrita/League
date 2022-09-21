package com.example.league

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.league.databinding.ActivityMainBinding
import com.example.league.repository.PostRepoImpl
import com.example.league.repository.UserRepoImpl
import com.example.league.viewModel.UserDetailsViewModel
import com.example.league.viewModel.UserDetailsViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by lazy {
        ViewModelProvider(this, UserDetailsViewModelFactory(
            userRepo = UserRepoImpl(), postRepo = PostRepoImpl()
        ))[UserDetailsViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeObservers()
        getUsers()
    }

    private fun initializeObservers() {
        viewModel.userDetailsObserver.observe(this as LifecycleOwner, Observer{
            val users = it.size
            val name = it[0].userName
            val body = it[0].body
        })

        viewModel.userDetailsLiveDataErrorObserver.observe(this as LifecycleOwner,Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun getUsers() {
        viewModel.get()
    }
}