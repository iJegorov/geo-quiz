package com.example.geoquiz.data.repository

import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class QuestionRepositoryTest {

    private lateinit var repository: QuestionRepository

    @Before
    fun setup() {
        repository = QuestionRepository(
            ApplicationProvider.getApplicationContext()
        )
    }

    @Test
    fun wrong_answer_increases_weight() {

        val question =
            repository.getQuestions("Easy", 1).first()

        repository.updateQuestionPerformance(
            question = question,
            correct = false
        )

        assertEquals(
            3,
            repository.getQuestionWeight(question.id)
        )
    }

    @Test
    fun correct_answer_decreases_weight() {

        val question =
            repository.getQuestions("Easy", 1).first()

        repository.updateQuestionPerformance(question, false)
        repository.updateQuestionPerformance(question, false)

        // 1 -> 3 -> 5

        repository.updateQuestionPerformance(question, true)

        // 5 -> 4

        assertEquals(
            4,
            repository.getQuestionWeight(question.id)
        )
    }

    @Test
    fun weight_never_exceeds_ten() {

        val question =
            repository.getQuestions("Easy", 1).first()

        repeat(20) {
            repository.updateQuestionPerformance(
                question,
                false
            )
        }

        assertEquals(
            10,
            repository.getQuestionWeight(question.id)
        )
    }

    @Test
    fun weight_never_goes_below_one() {

        val question =
            repository.getQuestions("Easy", 1).first()

        repeat(20) {
            repository.updateQuestionPerformance(
                question,
                true
            )
        }

        assertEquals(
            1,
            repository.getQuestionWeight(question.id)
        )
    }

    @Test
    fun questions_are_filtered_by_difficulty() {

        val questions =
            repository.getQuestions("Easy", 50)

        assertTrue(
            questions.all {
                it.difficulty.equals(
                    "Easy",
                    ignoreCase = true
                )
            }
        )
    }

    @Test
    fun quiz_contains_only_unique_questions() {

        val result = repository.getQuestions("Easy", 7)

        val uniqueIds = result.map { it.id }.toSet()

        assertEquals(
            result.size,
            uniqueIds.size
        )
    }

    @Test
    fun quiz_respects_limit_size() {

        val result = repository.getQuestions("Easy", 7)

        assertTrue(result.size <= 7)
    }
}