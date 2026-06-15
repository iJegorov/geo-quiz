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



/**
 * QuizViewModel
 *
 * Central state holder for application.
 *
 * Responsibilities:
 * - Manages quiz progression (questions, index, answers)
 * - Tracks score and statistics
 * - Persists long-term user stats via DataStore
 * - Sends question selection tasks to QuizEngine (supports adaptive learning)
 *
 * Architecture:
 * - MVVM (ViewModel layer)
 * - Dependency Injection via constructor (QuizEngine)
 */

class QuizViewModel(
    application: Application,
    private val quizEngine: QuizEngine
) : AndroidViewModel(application) {

    // ---------------------------------------------------------
    // DATA STORE (persistent statistics storage)
    // ---------------------------------------------------------
    private val statsDataStore = StatsDataStore(application)

    // ---------------------------------------------------------
    // QUIZ STATE (runtime only)
    // ---------------------------------------------------------
    /** Current question list for active quiz session */
    private var questions by mutableStateOf(listOf<Question>())

    /** Index of the currently displayed question */
    var currentQuestionIndex by mutableStateOf(0)
        private set

    /** Number of correctly answered questions in current quiz */
    var score by mutableStateOf(0)
        private set

    /** Selected difficulty level (Easy / Medium / Hard) */
    var selectedDifficulty by mutableStateOf("Easy")
        private set

    /**
     * Returns current question safely.
     * Provides fallback placeholder if list is empty.
     */
    val currentQuestion: Question
        get() = questions.getOrNull(currentQuestionIndex)
            ?: Question(
                id = -1,
                difficulty = "Easy",
                questionText = "Loading question...",
                options = listOf("N/A"),
                correctAnswer = "N/A"
            )


    // ---------------------------------------------------------
    // USER STATISTICS (persisted across sessions)
    // ---------------------------------------------------------
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


    // ---------------------------------------------------------
    // UI STATE (current quiz interaction state)
    // ---------------------------------------------------------
    /** Currently selected answer option */
    var selectedAnswer by mutableStateOf<String?>(null)
        private set
    /** Whether user has submitted an answer for current question */
    var answerSubmitted by mutableStateOf(false)
        private set
    /** Total points accumulated (persistent progression system) */
    var totalPoints by mutableStateOf(0)
        private set
    /** Points gained in current quiz session */
    var currentQuizPoints by mutableStateOf(0)
        private set
    /**
     * User level derived from total points.
     * Delegated to LevelCalculator for separation of concerns.
     */
    val currentLevel: String
        get() = LevelCalculator.getLevel(totalPoints)


    // ---------------------------------------------------------
    // INITIALIZATION
    // ---------------------------------------------------------
    init {
        setDifficulty("Easy")

        // Load persistent stats asynchronously
        viewModelScope.launch {
            val saved = statsDataStore.loadStats()

            totalPoints = saved.totalPoints
            totalQuizzesPlayed = saved.totalQuizzes
            bestScore = saved.bestScore
            totalCorrectAnswers = saved.totalCorrect
            totalQuestionsAnswered = saved.totalAnswered
        }
    }


    // ---------------------------------------------------------
    // QUIZ SETUP / DIFFICULTY HANDLING
    // ---------------------------------------------------------

    /**
     * Loads a new quiz session based on difficulty.
     *
     * Delegates question selection to QuizEngine,
     */
    fun setDifficulty(difficulty: String) {
        selectedDifficulty = difficulty
        questions = quizEngine.getQuestions(difficulty, 7)
        currentQuestionIndex = 0
        score = 0
        selectedAnswer = null
        answerSubmitted = false
        currentQuizPoints = 0
    }


    // ---------------------------------------------------------
    // ANSWER HANDLING LOGIC
    // ---------------------------------------------------------

    /**
     * Processes user answer selection.
     *
     * Responsibilities:
     * - Checks correctness
     * - Updates adaptive learning engine (question weighting)
     * - Updates score and statistics
     * - Updates points system
     */
    fun answerQuestion(answer: String) {

        if (answerSubmitted) return

        selectedAnswer = answer
        answerSubmitted = true

        val isCorrect = answer == currentQuestion.correctAnswer
        quizEngine.updateQuestionPerformance(currentQuestion, isCorrect)

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


    // ---------------------------------------------------------
    // NAVIGATION BETWEEN QUESTIONS
    // ---------------------------------------------------------

    /**
     * Moves to next question.
     *
     * @return true if quiz is finished (last question reached)
     */
    fun moveToNextQuestion(): Boolean {

        val isLast = currentQuestionIndex == questions.lastIndex
        if (!isLast) currentQuestionIndex++
        selectedAnswer = null
        answerSubmitted = false

        return isLast
    }


    // ---------------------------------------------------------
    // QUIZ COMPLETION
    // ---------------------------------------------------------

    /**
     * Finalizes quiz session and updates long-term stats.
     */
    fun finalizeQuiz() {

        totalQuizzesPlayed++
        lastScore = score
        lastDifficultyPlayed = selectedDifficulty

        if (score > bestScore) bestScore = score

        saveStats()
    }

    /**
     * Persists stats using DataStore.
     */
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


    // ---------------------------------------------------------
    // RESET / UTILITIES
    // ---------------------------------------------------------

    /**
     * Resets current quiz while keeping difficulty.
     */
    fun resetQuiz() {
        setDifficulty(selectedDifficulty)
    }

    /** Returns total number of questions in current quiz */
    fun getTotalQuestions(): Int = questions.size

    /** Checks if current question is last in the quiz */
    fun isLastQuestion(): Boolean =
        currentQuestionIndex >= questions.lastIndex
}