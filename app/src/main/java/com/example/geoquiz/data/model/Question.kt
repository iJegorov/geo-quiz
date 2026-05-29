package com.example.geoquiz.data.model

data class Question(
    val difficulty: String,
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String
)