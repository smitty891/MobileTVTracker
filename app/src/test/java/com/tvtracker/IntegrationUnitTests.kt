package com.tvtracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tvtracker.dto.ImdbResponse
import com.tvtracker.service.MediaService
import com.tvtracker.ui.main.BrowseViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class IntegrationUnitTests {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var bvm : BrowseViewModel

    @MockK
    lateinit var mockMediaService : MediaService

    private val mainThreadSurrogate = newSingleThreadContext("Main Thread")

    @Before
    fun initMocksAndMainThread() {
        MockKAnnotations.init(this)
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
   