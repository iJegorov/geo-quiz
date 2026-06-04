package com.example.geoquiz.domain


import org.junit.Assert.assertEquals
import org.junit.Test

class ScoreCalculatorTest {

    @Test
    fun easy_correct_returns_10_points() {
        val result = ScoreCalculator.getCorrectPoints("Easy")
        assertEquals(10, result)
    }

    @Test
    fun medium_correct_returns_30_points() {
        val result = ScoreCalculator.getCorrectPoints("Medium")
        assertEquals(30, result)
    }

    @Test
    fun hard_correct_returns_50_points() {
        val result = ScoreCalculator.getCorrectPoints("Hard")
        assertEquals(50, result)
    }

    @Test
    fun easy_wrong_returns_5_penalty() {
        val result = ScoreCalculator.getWrongPenalty("Easy")
        assertEquals(5, result)
    }

    @Test
    fun medium_wrong_returns_15_penalty() {
        val result = ScoreCalculator.getWrongPenalty("Medium")
        assertEquals(15, result)
    }

    @Test
    fun hard_wrong_returns_25_penalty() {
        val result = ScoreCalculator.getWrongPenalty("Hard")
        assertEquals(25, result)
    }
}