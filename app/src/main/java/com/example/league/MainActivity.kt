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

/* ***********This application is a single activity application.******************
Since there is only one screen, I didn't add any fragment.

********************Design Pattern***************
MVVM design pattern.
View is represented by activity.
Data is represented by models.
Repository module is responsible for talking to the apis.
NetworkResult and NetworkUtils has been used to capture the success and error response from api.
The business logic resides in ViewModels.

********************Libraries used in this application***************
Coroutines with live data and Retrofit are used to talk to the api server.
Gson converter is used to convert the incoming response to JSON objects.
Coil for rendering avatar view.

********************Error handling and unit testing***************
Error scenarios are handled by displaying Toast.
JUnit test framework is used for the view model tests.

*********************Extra functionality:****************************
The app is RTL language compatible.

*********************Some hacks in the interest of expediency:****************************
The strings and dimens are hard coded in the code instead of using the res folders.
ViewModels directly call Repository functions. Since there is only one screen, I didn't
use UseCase. If such a need arises and also for better scalability of the app, UseCases could be used.
Dependency injection was not used.
* */

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