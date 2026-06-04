package com.example.geoquiz.data.datastore

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StatsDataStoreTest {

    private lateinit var dataStore: StatsDataStore

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        dataStore = StatsDataStore(context)
    }

    @Test
    fun save_and_load_stats_returns_same_values() = runBlocking {

        dataStore.saveStats(
            totalPoints = 100,
            totalQuizzes = 5,
            bestScore = 7,
            totalCorrect = 20,
            totalAnswered = 30
        )

        val stats = dataStore.loadStats()

        assertEquals(100, stats.totalPoints)
        assertEquals(5, stats.totalQuizzes)
        assertEquals(7, stats.bestScore)
        assertEquals(20, stats.totalCorrect)
        assertEquals(30, stats.totalAnswered)
    }

    @Test
    fun default_values_are_zero() = runBlocking {

        val stats = dataStore.loadStats()

        assert(stats.totalPoints >= 0)
        assert(stats.totalQuizzes >= 0)
        assert(stats.bestScore >= 0)
        assert(stats.totalCorrect >= 0)
        assert(stats.totalAnswered >= 0)
    }
}