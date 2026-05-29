package com.example.geoquiz.data.repository

import android.content.Context
import com.example.geoquiz.data.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

object QuestionRepository {

    private var cachedQuestions: List<Question>? = null

    fun loadQuestions(context: Context): List<Question> {

        if (cachedQuestions != null) {
            return cachedQuestions!!
        }

        val inputStream = context.assets.open("questions.json")
        val reader = InputStreamReader(inputStream)

        val type = object : TypeToken<List<Question>>() {}.type

        cachedQuestions = Gson().fromJson(reader, type)

        reader.close()

        return cachedQuestions!!
    }

    fun getQuestionsByDifficulty(
        context: Context,
        difficulty: String
    ): List<Question> {

        return loadQuestions(context).filter {
            it.difficulty == difficulty
        }
    }
}