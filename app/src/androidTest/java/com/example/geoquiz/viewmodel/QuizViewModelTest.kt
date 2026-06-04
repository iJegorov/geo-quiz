package com.example.geoquiz.viewmodel

import androidx.test.core.app.ApplicationProvider
import com.example.geoquiz.data.model.Question
import com.example.geoquiz.data.quiz.QuizEngine
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

// ------------------------------
// FAKE ENGINE
// ------------------------------
class FakeQuizEngine : QuizEngine {

    var questions: List<Question> = emptyList()

    override fun getQuestions(difficulty: String, limit: Int): List<Question> {
        return questions
            .filter { it.difficulty == difficulty }
            .take(limit)
    }
}

// ------------------------------
// TEST CLASS
// ------------------------------
class QuizViewModelTest {

    private lateinit var fakeEngine: FakeQuizEngine
    private lateinit var viewModel: QuizViewModel

    private val sampleQuestions = listOf(
        Question(
            id = 1,
            difficulty = "Easy",
            questionText = "Capital of France?",
            options = listOf("Paris", "Berlin", "Rome", "Madrid"),
            correctAnswer = "Paris"
        ),
        Question(
            id = 2,
            difficulty = "Easy",
            questionText = "Capital of Germany?",
            options = listOf("Paris", "Berlin", "Rome", "Madrid"),
            correctAnswer = "Berlin"
        )
    )

    @Before
    fun setup() {

        fakeEngine = FakeQuizEngine().apply {
            questions = sampleQuestions
        }

        viewModel = QuizViewModel(
            application = ApplicationProvider.getApplicationContext(),
            quizEngine = fakeEngine
        )
    }

    @Test
    fun initial_state_is_correct() {

        assertEquals(0, viewModel.score)
        assertEquals("Easy", viewModel.selectedDifficulty)
        assertFalse(viewModel.answerSubmitted)
        assertEquals(0, viewModel.currentQuestionIndex)
    }

    @Test
    fun questions_are_loaded_from_engine() {

        assertEquals(2, viewModel.getTotalQuestions())
        assertEquals("Capital of France?", viewModel.currentQuestion.questionText)
    }

    @Test
    fun answering_correct_question_increases_score() {

        viewModel.answerQuestion("Paris")

        assertEquals(1, viewModel.score)
        assertTrue(viewModel.answerSubmitted)
        assertEquals("Paris", viewModel.selectedAnswer)
    }

    @Test
    fun answering_wrong_question_does_not_increase_score() {

        viewModel.answerQuestion("Berlin")

        assertEquals(0, viewModel.score)
        assertTrue(viewModel.answerSubmitted)
        assertEquals("Berlin", viewModel.selectedAnswer)
    }

    @Test
    fun move_to_next_question_works() {

        val first = viewModel.currentQuestion.questionText

        viewModel.moveToNextQuestion()

        val second = viewModel.currentQuestion.questionText

        assertNotEquals(first, second)
        assertEquals(1, viewModel.currentQuestionIndex)
    }

    @Test
    fun reset_quiz_resets_state() {

        viewModel.answerQuestion("Paris")
        viewModel.moveToNextQuestion()

        viewModel.resetQuiz()

        assertEquals(0, viewModel.currentQuestionIndex)
        assertEquals(0, viewModel.score)
        assertFalse(viewModel.answerSubmitted)
    }

    @Test
    fun detects_last_question_correctly() {

        assertFalse(viewModel.isLastQuestion())

        viewModel.moveToNextQuestion()

        assertTrue(viewModel.isLastQuestion())
    }
}