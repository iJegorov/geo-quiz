package com.example.geoquiz.domain

object LevelCalculator {

    fun getLevel(points: Int): String {
        return when {
            points >= 5000 -> "Cartographer"
            points >= 1000 -> "Professional"
            points >= 500 -> "Intermediate"
            points >= 100 -> "Beginner"
            else -> "Novice"
        }
    }
}