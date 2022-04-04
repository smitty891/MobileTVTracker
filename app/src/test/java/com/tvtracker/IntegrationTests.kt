package com.tvtracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tvtracker.dto.ImdbResponse
import com.tvtracker.service.MediaService
import com.tvtracker.ui.main.BrowseViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import junit.framework.Assert.*
import org.junit.rules.TestRule

class IntegrationTests {

    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var mediaService: MediaService
    var imdbResponse: ImdbResponse? = null

    @Test
    fun `Given media data is available when I search BattleBots then I should receive a confirmation response` () = runTest {
        givenMediaServiceIsInitialized()
        whenUserSearchesBattleBots()
        thenMediaResultsContainsBattleBots()
    }
    private fun givenMediaServiceIsInitialized() {
        mediaService = MediaService()
    }

    private suspend fun whenUserSearchesBattleBots() {
        imdbResponse = mediaService.searchImdb("BattleBots", "series", 1)
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