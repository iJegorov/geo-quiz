package com.example.geoquiz.data.repository

import android.content.Context
import com.example.geoquiz.data.model.Question
import com.example.geoquiz.data.quiz.QuizEngine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class QuestionRepository(
    private val context: Context
) : QuizEngine {

    private var cachedQuestions: List<Question>? = null
    private val questionWeightMap = mutableMapOf<Int, Int>()

    // -------------------------
    // LOAD ONCE
    // -------------------------
    private fun loadQuestions(): List<Question> {

        if (cachedQuestions != null) return cachedQuestions!!

        val inputStream = context.assets.open("questions.json")
        val reader = InputStreamReader(inputStream)

        val type = object : TypeToken<List<Question>>() {}.type
        cachedQuestions = Gson().fromJson(reader, type)

        reader.close()

        cachedQuestions!!.forEach { q ->
            questionWeightMap[q.id] = 1
        }

        return cachedQuestions!!
    }

    // -------------------------
    // ENGINE IMPLEMENTATION
    // -------------------------
    override fun getQuestions(
        difficulty: String,
        limit: Int
    ): List<Question> {

        val base = loadQuestions().filter {
            it.difficulty.equals(difficulty, ignoreCase = true)
        }

        // Weighted pool (still used for probability bias)
        val weightedPool = base.flatMap { q ->
            val weight = questionWeightMap[q.id] ?: 1
            List(weight) { q }
        }

        val result = mutableListOf<Question>()
        val usedIds = mutableSetOf<Int>()

        // Keep picking until we have UNIQUE questions
        val shuffled = weightedPool.shuffled()

        for (q in shuffled) {

            if (q.id !in usedIds) {
                result.add(q)
                usedIds.add(q.id)
            }

            if (result.size == limit) break
        }

        return result
    }

    // -------------------------
    // ADAPTIVE LEARNING
    // -------------------------
    override fun updateQuestionPerformance(
        question: Question,
        correct: Boolean
    ){
        val current = questionWeightMap[question.id] ?: 1

        questionWeightMap[question.id] = if (correct) {
            (current - 1).coerceAtLeast(1)
        } else {
            (current + 2).coerceAtMost(10)
        }
    }

    // -------------------------
    // TEST SUPPORT ONLY
    // -------------------------
    fun getQuestionWeight(questionId: Int): Int {
        return questionWeightMap[questionId] ?: 1
    }
}