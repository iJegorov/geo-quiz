package com.example.geoquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.geoquiz.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel
) {

    val question = viewModel.currentQuestion

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🎮 Player progression info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = viewModel.currentLevel,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${viewModel.totalPoints} pts",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ❓ Question text
        Text(
            text = question.questionText,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 📊 Quiz statistics
        Text(
            text = "Correct answers: ${viewModel.score}"
        )

        Text(
            text = "Quiz points: ${viewModel.currentQuizPoints}"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 📍 Progress indicator
        Text(
            text = "Question ${viewModel.currentQuestionIndex + 1} / ${viewModel.getTotalQuestions()}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🔘 Answer buttons
        question.options.forEach { option ->

            val isCorrectAnswer = option == question.correctAnswer
            val isSelected = option == viewModel.selectedAnswer

            Button(
                onClick = {
                    viewModel.answerQuestion(option)
                },

                enabled = !viewModel.answerSubmitted,

                colors = ButtonDefaults.buttonColors(

                    containerColor = when {

                        // ✅ Correct answer
                        viewModel.answerSubmitted && isCorrectAnswer ->
                            MaterialTheme.colorScheme.primary

                        // ❌ Wrong selected answer
                        viewModel.answerSubmitted && isSelected ->
                            MaterialTheme.colorScheme.error

                        else ->
                            MaterialTheme.colorScheme.secondary
                    }
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(option)

                    if (viewModel.answerSubmitted) {

                        when {

                            // ✅ Correct answer
                            option == question.correctAnswer -> {

                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Correct Answer"
                                )
                            }

                            // ❌ Wrong selected answer
                            option == viewModel.selectedAnswer -> {

                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Wrong Answer"
                                )
                            }
                        }
                    }
                }
            }
        }

        // ➡ Continue button
        if (viewModel.answerSubmitted) {

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {

                    val isFinished = viewModel.moveToNextQuestion()

                    if (isFinished) {

                        viewModel.finalizeQuiz()

                        navController.navigate("result") {
                            popUpTo("quiz") { inclusive = true }
                        }
                    }
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Continue")
            }
        }
    }
}