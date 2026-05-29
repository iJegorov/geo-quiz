package com.example.geoquiz.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.geoquiz.data.model.Question
import com.example.geoquiz.data.repository.QuestionRepository
import com.example.geoquiz.data.datastore.StatsDataStore
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val statsDataStore = StatsDataStore(application)

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
                id = -1,
                difficulty = "Easy",
                questionText = "Loading question...",
                options = listOf("N/A"),
                correctAnswer = "N/A"
            )

    // -------------------------
    // STATS
    // -------------------------
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

    // -------------------------
    // ANSWER STATE
    // -------------------------
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

        viewModelScope.launch {
            val savedStats = statsDataStore.loadStats()

            totalPoints = savedStats.totalPoints
            totalQuizzesPlayed = savedStats.totalQuizzes
            bestScore = savedStats.bestScore
            totalCorrectAnswers = savedStats.totalCorrect
            totalQuestionsAnswered = savedStats.totalAnswered
        }
    }

    // -------------------------
    // DIFFICULTY + QUESTION LOAD
    // -------------------------
    fun setDifficulty(difficulty: String) {
        selectedDifficulty = difficulty

        // ✅ IMPORTANT CHANGE: use weighted system instead of plain shuffle
        questions = QuestionRepository
            .getWeightedQuestionsByDifficulty(
                getApplication(),
                difficulty,
                limit = 7
            )

        currentQuestionIndex = 0
        score = 0

        selectedAnswer = null
        answerSubmitted = false
        currentQuizPoints = 0
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

    // -------------------------
    // ANSWER LOGIC
    // -------------------------
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

            if (totalPoints < 0) totalPoints = 0
            if (currentQuizPoints < 0) currentQuizPoints = 0
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

        saveStatistics()
    }

    private fun saveStatistics() {
        viewModelScope.launch {
            statsDataStore.saveStats(
                totalPoints = totalPoints,
                totalQuizzes = totalQuizzesPlayed,
                bestScore = bestScore,
                totalCorrect = totalCorrectAnswers,
                totalAnswered = totalQuestionsAnswered
            )
        }
    }

    fun resetQuiz() {
        setDifficulty(selectedDifficulty)
    }

    fun getTotalQuestions(): Int = questions.size

    fun isLastQuestion(): Boolean =
        currentQuestionIndex >= questions.lastIndex
}