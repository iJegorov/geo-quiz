package com.example.geoquiz.data.repository

import android.content.Context
import com.example.geoquiz.data.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

object QuestionRepository {

    private var cachedQuestions: List<Question>? = null

    // Adaptive weights (runtime only for now)
    private val questionWeightMap = mutableMapOf<Int, Int>()

    // --------------------------
    // LOAD QUESTIONS
    // --------------------------
    private fun loadQuestions(context: Context): List<Question> {

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

    // --------------------------
    // FILTER BY DIFFICULTY
    // --------------------------
    fun getQuestionsByDifficulty(
        context: Context,
        difficulty: String
    ): List<Question> {

        return loadQuestions(context).filter {
            it.difficulty == difficulty
        }
    }

    // --------------------------
    // UPDATE PERFORMANCE (adaptive learning)
    // --------------------------
    fun updateQuestionPerformance(question: Question, correct: Boolean) {

        val current = questionWeightMap[question.id] ?: 1

        questionWeightMap[question.id] = if (correct) {
            // improve mastery → reduce appearance
            (current - 1).coerceAtLeast(1)
        } else {
            // reinforce weakness → increase appearance
            (current + 2).coerceAtMost(10)
        }
    }

    // --------------------------
    // WEIGHTED SELECTION (MAIN FEATURE)
    // --------------------------
    fun getWeightedQuestionsByDifficulty(
        context: Context,
        difficulty: String,
        limit: Int = 7
    ): List<Question> {

        val base = getQuestionsByDifficulty(context, difficulty)

        val weightedPool = base.flatMap { q ->
            val weight = questionWeightMap[q.id] ?: 1
            List(weight) { q }
        }

        return weightedPool.shuffled().take(limit)
    }
}