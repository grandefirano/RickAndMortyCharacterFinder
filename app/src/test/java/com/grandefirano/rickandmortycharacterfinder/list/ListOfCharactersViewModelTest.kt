package com.grandefirano.rickandmortycharacterfinder.list

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.grandefirano.rickandmortycharacterfinder.CoroutinesTestRule
import com.grandefirano.rickandmortycharacterfinder.data.Character
import com.grandefirano.rickandmortycharacterfinder.data.RepositoryImpl
import com.grandefirano.rickandmortycharacterfinder.data.Search
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
class ListOfCharactersViewModelTest {

    private lateinit var repository: RepositoryImpl
    private lateinit var viewModel:ListOfCharactersViewModel
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @get:Rule
    var instantExecutorRule=InstantTaskExecutorRule()

    @Before
    fun setUp() {
        repository=mock(RepositoryImpl::class.java)
        viewModel= ListOfCharactersViewModel(repository,testScope)
    }

    @Test
    fun navigateToCharacterDetails_passCharacterToLiveData_Equal() {

        val character=mock(Character::class.java)
       viewModel.onCharacterClicked(character)
        assertThat(viewModel.navigateToCharacterDetail.getOrAwaitValue(),`is`(equalTo(character)))
    }
    @Test
    fun navigateToCharacterDetails_passWrongCharacterToLiveData_notEqual() {


        val character=mock(Character::class.java)
        val character2=mock(Character::class.java)
        viewModel.onCharacterClicked(character)

        assertThat(viewModel.navigateToCharacterDetail.getOrAwaitValue(),`is`(not(equalTo(character2))))
    }


    @Test
    fun onQueryTextChanged_Equal() {
        val name="Rick"
        viewModel.onQueryTextChanged(name)

        assertThat(viewModel.searchRequest.getOrAwaitValue().name,`is`(equalTo(name)))
    }
    @Test
    fun onQueryTextChanged_NotEqual() {
        val name="Rick"
        val name2="Jeez"
        viewModel.onQueryTextChanged(name)

        assertThat(viewModel.searchRequest.getOrAwaitValue().name,`is`(not(equalTo(name2))))
    }

    @Test
    fun onQueryGenderChanged_Equal() {
        val gender=Search.GenderOption.FEMALE
        viewModel.onQueryGenderChanged(gender)

        assertThat(viewModel.searchRequest.getOrAwaitValue().gender,`is`(equalTo(gender)))
    }
    @Test
    fun onQueryGenderChanged_NotEqual() {
        val gender=Search.GenderOption.FEMALE
        val gender2=Search.GenderOption.GENDERLESS
        viewModel.onQueryGenderChanged(gender)

        assertThat(viewModel.searchRequest.getOrAwaitValue().gender,`is`(not(equalTo(gender2))))
    }

    @Test
    fun onQueryStatusChanged_Equal() {
        val status=Search.StatusOption.DEAD
        viewModel.onQueryStatusChanged(status)

        assertThat(viewModel.searchRequest.getOrAwaitValue().status,`is`(equalTo(status)))
    }
    @Test
    fun onQueryStatusChanged_NotEqual() {
        val status=Search.StatusOption.DEAD
        val status2=Search.StatusOption.ALIVE
        viewModel.onQueryStatusChanged(status)

        assertThat(viewModel.searchRequest.getOrAwaitValue().status,`is`(not(equalTo(status2))))
    }



}

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}