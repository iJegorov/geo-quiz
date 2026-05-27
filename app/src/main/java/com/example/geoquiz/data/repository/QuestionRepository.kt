package com.example.geoquiz.data.repository

import com.example.geoquiz.data.model.Question

object QuestionRepository {

    fun getQuestions(difficulty: String): List<Question> {

        return when (difficulty) {

            "Easy" -> easyQuestions()

            "Medium" -> mediumQuestions()

            "Hard" -> hardQuestions()

            else -> easyQuestions()
        }
    }

    private fun easyQuestions(): List<Question> {

        return listOf(

            Question(
                "What is the capital of France?",
                listOf("Paris", "Berlin", "Madrid", "Rome"),
                "Paris"
            ),

            Question(
                "Which country has the maple leaf flag?",
                listOf("USA", "Canada", "Australia", "UK"),
                "Canada"
            ),

            Question(
                "Which country is Tokyo the capital of?",
                listOf("China", "Japan", "Thailand", "South Korea"),
                "Japan"
            )
        )
    }

    private fun mediumQuestions(): List<Question> {

        return listOf(

            Question(
                "What is the capital of Argentina?",
                listOf("Buenos Aires", "Lima", "Santiago", "Bogota"),
                "Buenos Aires"
            ),

            Question(
                "Which country has the city of Marrakech?",
                listOf("Egypt", "Morocco", "Tunisia", "Turkey"),
                "Morocco"
            ),

            Question(
                "What is the largest country in Africa?",
                listOf("Nigeria", "Algeria", "Sudan", "Egypt"),
                "Algeria"
            )
        )
    }

    private fun hardQuestions(): List<Question> {

        return listOf(

            Question(
                "What is the capital of Kazakhstan?",
                listOf("Astana", "Tashkent", "Bishkek", "Minsk"),
                "Astana"
            ),

            Question(
                "Which country owns the Faroe Islands?",
                listOf("Norway", "Denmark", "Iceland", "Finland"),
                "Denmark"
            ),

            Question(
                "What is the smallest country in Africa?",
                listOf("Seychelles", "Gambia", "Comoros", "Djibouti"),
                "Seychelles"
            )
        )
    }
}