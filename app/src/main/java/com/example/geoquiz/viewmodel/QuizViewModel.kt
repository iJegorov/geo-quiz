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

    init {
        // ✅ Single source of truth initialization
        setDifficulty("Easy")
    }

    fun setDifficulty(difficulty: String) {
        selectedDifficulty = difficulty

        questions = QuestionRepository.getQuestions(difficulty)

        currentQuestionIndex = 0
        score = 0
    }

    fun answerQuestion(answer: String): Boolean {

        val isCorrect = answer == currentQuestion.correctAnswer

        Log.d("QuizDebug", "Is correct = $isCorrect")

        if (isCorrect) {
            score++
        }

        val isLast = currentQuestionIndex == questions.lastIndex

        if (!isLast) {
            currentQuestionIndex++
        }

        return isLast
    }

    fun resetQuiz() {
        setDifficulty(selectedDifficulty)
    }

    fun getTotalQuestions(): Int {
        return questions.size
    }

    fun isLastQuestion(): Boolean {
        return currentQuestionIndex >= questions.lastIndex
    }
}