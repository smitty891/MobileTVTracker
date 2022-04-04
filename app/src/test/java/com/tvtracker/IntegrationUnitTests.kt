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
}