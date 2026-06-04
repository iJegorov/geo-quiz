package com.example.geoquiz.data.quiz

import com.example.geoquiz.data.model.Question

interface QuizEngine {
    fun getQuestions(difficulty: String, limit: Int): List<Question>
}