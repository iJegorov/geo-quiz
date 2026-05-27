package com.example.geoquiz.data.model

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String
)