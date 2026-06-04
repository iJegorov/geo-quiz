package com.example.geoquiz.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoquiz.data.datastore.StatsDataStore
import com.example.geoquiz.data.model.Question
import com.example.geoquiz.data.quiz.QuizEngine
import com.example.geoquiz.domain.LevelCalculator
import com.example.geoquiz.domain.ScoreCalculator
import kotlinx.coroutines.launch

class QuizViewModel(
    application: Application,
    private val quizEngine: QuizEngine
) : AndroidViewModel(application) {

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
    // UI STATE
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
        get() = LevelCalculator.getLevel(totalPoints)

    init {
        setDifficulty("Easy")

        viewModelScope.launch {
            val saved = statsDataStore.loadStats()

            totalPoints = saved.totalPoints
            totalQuizzesPlayed = saved.totalQuizzes
            bestScore = saved.bestScore
            totalCorrectAnswers = saved.totalCorrect
            totalQuestionsAnswered = saved.totalAnswered
        }
    }

    // -------------------------
    // QUESTIONS
    // -------------------------
    fun setDifficulty(difficulty: String) {

        selectedDifficulty = difficulty

        questions = quizEngine.getQuestions(difficulty, 7)

        currentQuestionIndex = 0
        score = 0

        selectedAnswer = null
        answerSubmitted = false
        currentQuizPoints = 0
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

            val points = ScoreCalculator.getCorrectPoints(selectedDifficulty)

            totalPoints += points
            currentQuizPoints += points

        } else {

            val penalty = ScoreCalculator.getWrongPenalty(selectedDifficulty)

            totalPoints -= penalty
            currentQuizPoints -= penalty

            if (totalPoints < 0) totalPoints = 0
            if (currentQuizPoints < 0) currentQuizPoints = 0
        }

        totalQuestionsAnswered++
    }

    fun moveToNextQuestion(): Boolean {

        val isLast = currentQuestionIndex == questions.lastIndex

        if (!isLast) currentQuestionIndex++

        selectedAnswer = null
        answerSubmitted = false

        return isLast
    }

    fun finalizeQuiz() {

        totalQuizzesPlayed++
        lastScore = score
        lastDifficultyPlayed = selectedDifficulty

        if (score > bestScore) bestScore = score

        saveStats()
    }

    private fun saveStats() {
        viewModelScope.launch {
            statsDataStore.saveStats(
                totalPoints,
                totalQuizzesPlayed,
                bestScore,
                totalCorrectAnswers,
                totalQuestionsAnswered
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