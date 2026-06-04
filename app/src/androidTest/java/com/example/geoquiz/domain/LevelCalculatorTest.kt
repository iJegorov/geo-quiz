package com.example.geoquiz.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class LevelCalculatorTest {

    @Test
    fun noviceBelow100Points() {
        assertEquals(
            "Novice",
            LevelCalculator.getLevel(99)
        )
    }

    @Test
    fun beginnerStartsAt100Points() {
        assertEquals(
            "Beginner",
            LevelCalculator.getLevel(100)
        )
    }

    @Test
    fun intermediateStartsAt500Points() {
        assertEquals(
            "Intermediate",
            LevelCalculator.getLevel(500)
        )
    }

    @Test
    fun professionalStartsAt1000Points() {
        assertEquals(
            "Professional",
            LevelCalculator.getLevel(1000)
        )
    }

    @Test
    fun cartographerStartsAt5000Points() {
        assertEquals(
            "Cartographer",
            LevelCalculator.getLevel(5000)
        )
    }

    @Test
    fun cartographerAbove5000Points() {
        assertEquals(
            "Cartographer",
            LevelCalculator.getLevel(10000)
        )
    }
}