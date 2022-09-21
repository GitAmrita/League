package com.example.league

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        initializeRecycleView()
        getUserDetails()
    }

    private fun initializeObservers() {
        viewModel.userDetailsObserver.observe(this as LifecycleOwner, Observer{
            binding.recycleView.adapter = UserDetailsAdapter(it!!)
            binding.recycleView.adapter ?.notifyDataSetChanged()
            binding.progressBar.visibility = View.GONE
        })

        viewModel.userDetailsLiveDataErrorObserver.observe(this as LifecycleOwner,Observer {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initializeRecycleView() {
        binding.recycleView.layoutManager = LinearLayoutManager(this)
    }

    private fun getUserDetails() {
        viewModel.get()
    }
}