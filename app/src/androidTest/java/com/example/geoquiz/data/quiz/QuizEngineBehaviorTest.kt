package com.example.geoquiz.data.quiz

import com.example.geoquiz.data.model.Question
import org.junit.Assert.*
import org.junit.Test

class FakeEngine : QuizEngine {

    var questions: List<Question> = emptyList()

    override fun getQuestions(difficulty: String, limit: Int): List<Question> {
        return questions
            .filter { it.difficulty == difficulty }
            .take(limit)
    }
}

class QuizEngineBehaviorTest {

    private val engine = FakeEngine()

    private val sampleData = listOf(
        Question(1, "Easy", "Q1", listOf("A","B"), "A"),
        Question(2, "Easy", "Q2", listOf("A","B"), "A"),
        Question(3, "Hard", "Q3", listOf("A","B"), "A")
    )

    @Test
    fun filters_by_difficulty_correctly() {

        engine.questions = sampleData

        val result = engine.getQuestions("Easy", 10)

        assertEquals(2, result.size)
        assertTrue(result.all { it.difficulty == "Easy" })
    }

    @Test
    fun respects_limit() {

        engine.questions = sampleData

        val result = engine.getQuestions("Easy", 1)

        assertEquals(1, result.size)
    }

    @Test
    fun returns_empty_if_no_match() {

        engine.questions = sampleData

        val result = engine.getQuestions("Extreme", 5)

        assertTrue(result.isEmpty())
    }
}