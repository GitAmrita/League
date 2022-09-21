package com.example.league

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.league.model.Post
import com.example.league.model.User
import com.example.league.network.NetworkResult
import com.example.league.repository.PostRepo
import com.example.league.repository.UserRepo
import com.example.league.viewModel.UserDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UserDetailsViewModelTest {
    private val userRepo = mock<UserRepo>()
    private val postRepo = mock<PostRepo>()
    private lateinit var viewModel: UserDetailsViewModel
    private lateinit var users: List<User>
    private lateinit var posts: List<Post>

    @Before
    fun setUp() {
        viewModel = UserDetailsViewModel(userRepo, postRepo)
        users = listOf(
            User(1, "abc", "http://12"),
            User(2, "yhu", "http://22"),
            User(3, "kil", "http://45")
        )
        posts = listOf(
            Post(1, "title1", "body1"),
            Post(1, "title2", "body2"),
            Post(2, "title12", "body12"),
            Post(2, "title122", "body122"))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    // The reason behind this rule is the lack of Looper.getMainLooper() on the testing environment
    // which is present on a real application. To fix this swap the Main dispatcher
    // with TestCoroutineDispatcher
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // A JUnit Test Rule that swaps the background executor used by the
    // Architecture Components with a different one which executes each task synchronously.
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `get returns success`() = runBlocking {
        whenever(userRepo.getUsers()).thenReturn(NetworkResult.Success(users))
        whenever(postRepo.getPosts()).thenReturn(NetworkResult.Success(posts))
        viewModel.get()
        val resource = viewModel.userDetailsObserver.value
        Assert.assertEquals(4, resource!!.size)
        Assert.assertTrue(resource.any {it.id == 1})
        Assert.assertNull(viewModel.userDetailsLiveDataErrorObserver.value)
    }

    @Test
    fun `get user call returns error post call returns error`() = runBlocking {
        whenever(userRepo.getUsers()).thenReturn(NetworkResult.Error("user error"))
        whenever(postRepo.getPosts()).thenReturn(NetworkResult.Error("post error"))
        viewModel.get()
        val resource = viewModel.userDetailsLiveDataErrorObserver.value
        Assert.assertEquals(" Unable to fetch results, try again later.",
            resource!!.toString())
        Assert.assertNull(viewModel.userDetailsObserver.value)
    }

    @Test
    fun `get user call returns error post call returns success`() = runBlocking {
        whenever(userRepo.getUsers()).thenReturn(NetworkResult.Error("user error"))
        whenever(postRepo.getPosts()).thenReturn(NetworkResult.Success(posts))
        viewModel.get()
        val resource = viewModel.userDetailsLiveDataErrorObserver.value
        Assert.assertEquals(" Unable to fetch results, try again later.",
            resource!!.toString())
        Assert.assertNull(viewModel.userDetailsObserver.value)
    }

    @Test
    fun `get post call returns error user call returns success`() = runBlocking {
        whenever(userRepo.getUsers()).thenReturn(NetworkResult.Success(users))
        whenever(postRepo.getPosts()).thenReturn(NetworkResult.Error("post error"))
        viewModel.get()
        val resource = viewModel.userDetailsLiveDataErrorObserver.value
        Assert.assertEquals(" Unable to fetch results, try again later.",
            resource!!.toString())
        Assert.assertNull(viewModel.userDetailsObserver.value)
    }

    @Test
    fun mapValues() {
        val result = viewModel.mapValues(users, posts)
        Assert.assertTrue(result.filter{it.id == 1}.size == 2)
        Assert.assertTrue(result.filter{it.id == 2}.size == 2)
        Assert.assertTrue(result.none { it.id == 3 })
    }
}