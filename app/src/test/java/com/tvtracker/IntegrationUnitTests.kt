package com.tvtracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import com.tvtracker.service.MediaService
import com.tvtracker.ui.main.BrowseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule



class IntegrationUnitTests {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var bvm : BrowseViewModel


    lateinit var mockMediaService : MediaService

    private val mainThreadSurrogate = newSingleThreadContext("Main Thread")

    @Before
    fun initMocksAndMainThread() {

        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `given a view model with live data when populated with media then results show the name BattleBots` () {
        givenViewModelIsInitializedWithMockData()
        whenMediaServiceFetchMoviesInvoked()
        thenMediaResultsContainsBattleBots()
    }

    private fun givenViewModelIsInitializedWithMockData() {

    }

    private fun  whenMediaServiceFetchMoviesInvoked() {

    }

    private fun  thenMediaResultsContainsBattleBots() {
    }
}