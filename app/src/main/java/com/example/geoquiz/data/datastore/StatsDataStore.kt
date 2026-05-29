package com.example.geoquiz.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "stats")

class StatsDataStore(private val context: Context) {

    companion object {

        val TOTAL_POINTS = intPreferencesKey("total_points")
        val TOTAL_QUIZZES = intPreferencesKey("total_quizzes")
        val BEST_SCORE = intPreferencesKey("best_score")
        val TOTAL_CORRECT = intPreferencesKey("total_correct")
        val TOTAL_ANSWERED = intPreferencesKey("total_answered")
    }

    suspend fun saveStats(
        totalPoints: Int,
        totalQuizzes: Int,
        bestScore: Int,
        totalCorrect: Int,
        totalAnswered: Int
    ) {

        context.dataStore.edit { preferences ->

            preferences[TOTAL_POINTS] = totalPoints
            preferences[TOTAL_QUIZZES] = totalQuizzes
            preferences[BEST_SCORE] = bestScore
            preferences[TOTAL_CORRECT] = totalCorrect
            preferences[TOTAL_ANSWERED] = totalAnswered
        }
    }

    suspend fun loadStats(): Stats {

        val preferences = context.dataStore.data.first()

        return Stats(
            totalPoints = preferences[TOTAL_POINTS] ?: 0,
            totalQuizzes = preferences[TOTAL_QUIZZES] ?: 0,
            bestScore = preferences[BEST_SCORE] ?: 0,
            totalCorrect = preferences[TOTAL_CORRECT] ?: 0,
            totalAnswered = preferences[TOTAL_ANSWERED] ?: 0
        )
    }
}

data class Stats(
    val totalPoints: Int,
    val totalQuizzes: Int,
    val bestScore: Int,
    val totalCorrect: Int,
    val totalAnswered: Int
)