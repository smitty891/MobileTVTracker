package com.tvtracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tvtracker.dto.ImdbResponse
import com.tvtracker.service.MediaService
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext

class IntegrationTests {

    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var bvm : BrowseViewModel //(add)

    @MockK //(add)
    lateinit var mockmediaService: mockMediaService //add

    var imdbResponse: ImdbResponse? = null
    private val mainThreadSurrogate = newSingleThreadContext("Main Thread") 

    @Before //add
    fun initMocksAndMainThread() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After //add
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }


    @Test
    fun `Given media data is available when I search BattleBots then I should receive media results`
                () = runTest {
        givenMediaServiceIsInitialized()
        whenUserSearchesBattleBots()
        thenMediaResultsContainsBattleBots()
    }
    private fun givenMediaServiceIsInitialized() {
        bvm = BrowseViewModel(mediaService = MediaService) //add
    }

    private suspend fun whenUserSearchesBattleBots() {
        imdbResponse = mediaService.searchIMDB("BattleBots", "series", 1)
    }

    private fun thenMediaResultsContainsBattleBots() {
        Assert.assertNotNull(imdbResponse)
        Assert.assertNotNull(imdbResponse!!.results)

        var foundBattleBots = false
        imdbResponse!!.results!!.forEach {
            if(it.title.uppercase().equals("BATTLEBOTS")){
                foundBattleBots = true
            }
        }

        Assert.assertTrue(foundBattleBots)
    }
}