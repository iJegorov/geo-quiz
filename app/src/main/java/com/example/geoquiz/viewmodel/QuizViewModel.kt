package com.example.geoquiz.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.geoquiz.data.model.Question
import com.example.geoquiz.data.repository.QuestionRepository
import android.util.Log

class QuizViewModel : ViewModel() {

    private var questions by mutableStateOf(listOf<Question>())

    var currentQuestionIndex by mutableStateOf(0)
        private set

    var score by mutableStateOf(0)
        private set

    var selectedDifficulty by mutableStateOf("Easy")
        private set

    val currentQuestion: Question
        get() = questions.getOrNull(currentQuestionIndex)
            ?: Question(
                "Loading question...",
                listOf("N/A"),
                "N/A"
            )

    // Statistics
    var totalQuizzesPlayed by mutableStateOf(0)
        private set

    var totalCorrectAnswers by mutableStateOf(0)
        private set

    var totalQuestionsAnswered by mutableStateOf(0)
        private set

    var bestScore by mutableStateOf(0)
        private set

    var lastScore by mutableStateOf(0)
        private set

    var lastDifficultyPlayed by mutableStateOf("Easy")
        private set

    // Answer state
    var selectedAnswer by mutableStateOf<String?>(null)
        private set

    var answerSubmitted by mutableStateOf(false)
        private set

    var totalPoints by mutableStateOf(0)
        private set

    var currentQuizPoints by mutableStateOf(0)
        private set

    val currentLevel: String
        get() = when {

            totalPoints >= 5000 -> "Cartographer"

            totalPoints >= 1000 -> "Professional"

            totalPoints >= 500 -> "Intermediate"

            totalPoints >= 100 -> "Beginner"

            else -> "Novice"
        }

    init {
        setDifficulty("Easy")
    }

    fun setDifficulty(difficulty: String) {
        selectedDifficulty = difficulty

        questions = QuestionRepository.getQuestions(difficulty)

        currentQuestionIndex = 0
        score = 0
    }

    private fun getCorrectPoints(): Int {

        return when (selectedDifficulty) {

            "Easy" -> 10

            "Medium" -> 30

            "Hard" -> 50

            else -> 10
        }
    }

    private fun getWrongPenalty(): Int {

        return when (selectedDifficulty) {

            "Easy" -> 5

            "Medium" -> 15

            "Hard" -> 25

            else -> 5
        }
    }

    fun answerQuestion(answer: String) {

        if (answerSubmitted) return

        selectedAnswer = answer
        answerSubmitted = true

        val isCorrect = answer == currentQuestion.correctAnswer

        if (isCorrect) {

            score++
            totalCorrectAnswers++

            val earnedPoints = getCorrectPoints()

            totalPoints += earnedPoints
            currentQuizPoints += earnedPoints

        } else {

            val penalty = getWrongPenalty()

            totalPoints -= penalty
            currentQuizPoints -= penalty

            // Prevent negative total points
            if (totalPoints < 0) {
                totalPoints = 0
            }
        }

        totalQuestionsAnswered++
    }


    fun moveToNextQuestion(): Boolean {

        val isLast = currentQuestionIndex == questions.lastIndex

        if (!isLast) {
            currentQuestionIndex++
        }

        selectedAnswer = null
        answerSubmitted = false

        return isLast
    }


    fun finalizeQuiz() {

        totalQuizzesPlayed++

        lastScore = score
        lastDifficultyPlayed = selectedDifficulty

        if (score > bestScore) {
            bestScore = score
        }
    }

    fun resetQuiz() {
        setDifficulty(selectedDifficulty)
        selectedAnswer = null
        answerSubmitted = false
        currentQuizPoints = 0
    }

    fun getTotalQuestions(): Int {
        return questions.size
    }

    fun isLastQuestion(): Boolean {
        return currentQuestionIndex >= questions.lastIndex
    }
}