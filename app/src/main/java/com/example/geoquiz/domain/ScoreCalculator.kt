package com.example.geoquiz.domain

object ScoreCalculator {

    fun getCorrectPoints(difficulty: String): Int {
        return when (difficulty) {
            "Easy" -> 10
            "Medium" -> 30
            "Hard" -> 50
            else -> 10
        }
    }

    fun getWrongPenalty(difficulty: String): Int {
        return when (difficulty) {
            "Easy" -> 5
            "Medium" -> 15
            "Hard" -> 25
            else -> 5
        }
    }
}