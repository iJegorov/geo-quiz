package com.example.geoquiz.data.quiz

import com.example.geoquiz.data.model.Question

/**
 * QuizEngine
 *
 * Core abstraction layer for quiz question retrieval and adaptive learning feedback.
 *
 * This interface defines the contract between:
 * - ViewModel (quiz logic consumer)
 * - Repository (data + adaptive behavior provider)
 *
 * It enables:
 * - Decoupling of UI logic from data source implementation
 * - Adaptive learning feedback loop
 */
interface QuizEngine {

    /**
     * Returns a list of questions for a given difficulty level.
     */
    fun getQuestions(
        difficulty: String,
        limit: Int
    ): List<Question>

    /**
     * Updates performance feedback for a question after user interaction.
     */
    fun updateQuestionPerformance(
        question: Question,
        correct: Boolean
    )
}