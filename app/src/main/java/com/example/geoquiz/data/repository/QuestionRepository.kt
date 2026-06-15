package com.example.geoquiz.data.repository

import android.content.Context
import com.example.geoquiz.data.model.Question
import com.example.geoquiz.data.quiz.QuizEngine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

/**
 * QuestionRepository
 *
 * Data source for quiz questions with adaptive learning support.
 *
 * Features:
 * - Loads questions from JSON file
 * - Provides weighted question selection (adaptive repetition)
 * - Ensures uniqeu questions per quiz session
 * - Updates question difficulty based on user performance
 */
class QuestionRepository(private val context: Context) : QuizEngine {

    private var cachedQuestions: List<Question>? = null
    private val questionWeightMap = mutableMapOf<Int, Int>()


    // ---------------------------------------------------------
    // LOAD QUESTIONS (SINGLE SOURCE OF TRUTH)
    // ---------------------------------------------------------
    private fun loadQuestions(): List<Question> {

        if (cachedQuestions != null) return cachedQuestions!!

        val inputStream = context.assets.open("questions.json")
        val reader = InputStreamReader(inputStream)

        val type = object : TypeToken<List<Question>>() {}.type
        cachedQuestions = Gson().fromJson(reader, type)

        reader.close()

        // initialize weights
        cachedQuestions!!.forEach { q ->
            questionWeightMap[q.id] = 1
        }

        return cachedQuestions!!
    }


    // ---------------------------------------------------------
    // QUIZ ENGINE (UNIQUE + WEIGHTED SELECTION)
    // ---------------------------------------------------------
    override fun getQuestions(
        difficulty: String,
        limit: Int
    ): List<Question> {

        val base = loadQuestions().filter {
            it.difficulty.equals(difficulty, ignoreCase = true)
        }

        // pool of available questions (no duplicates in final result)
        val available = base.toMutableList()
        val result = mutableListOf<Question>()

        repeat(limit.coerceAtMost(base.size)) {

            val weightedPool = available.flatMap { q ->
                val weight = questionWeightMap[q.id] ?: 1
                List(weight) { q }
            }

            val selected = weightedPool.random()

            result.add(selected)
            available.removeAll { it.id == selected.id }
        }

        return result
    }


    // ---------------------------------------------------------
    // ADAPTIVE LEARNING SYSTEM
    // ---------------------------------------------------------
    override fun updateQuestionPerformance(
        question: Question,
        correct: Boolean
    ) {
        val current = questionWeightMap[question.id] ?: 1

        questionWeightMap[question.id] = if (correct) {
            // correct → reduce frequency (fade out)
            (current - 1).coerceAtLeast(1)
        } else {
            // wrong → increase repetition
            (current + 2).coerceAtMost(10)
        }
    }


    // ---------------------------------------------------------
    // TESTING SUPPORT
    // ---------------------------------------------------------
    fun getQuestionWeight(questionId: Int): Int {
        return questionWeightMap[questionId] ?: 1
    }
}