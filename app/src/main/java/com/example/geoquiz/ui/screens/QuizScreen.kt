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
import androidx.compose.material3.contentColorFor
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
            .padding(20.dp)
    ) {

        // TOP INFO
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Level: ${viewModel.currentLevel}")
            Text("Points: ${viewModel.totalPoints}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // PROGRESS
        LinearProgressIndicator(
            progress = { (viewModel.currentQuestionIndex + 1f) / viewModel.getTotalQuestions() },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Question ${viewModel.currentQuestionIndex + 1} / ${viewModel.getTotalQuestions()}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // QUESTION CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Text(
                text = question.questionText,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // OPTIONS
        question.options.forEach { option ->

            val isCorrect = option == question.correctAnswer
            val isSelected = option == viewModel.selectedAnswer

            val containerColor = when {
                viewModel.answerSubmitted && isCorrect ->
                    MaterialTheme.colorScheme.primary

                viewModel.answerSubmitted && isSelected ->
                    MaterialTheme.colorScheme.error

                else ->
                    MaterialTheme.colorScheme.secondaryContainer
            }

            val contentColor = contentColorFor(containerColor)

            Button(
                onClick = { viewModel.answerQuestion(option) },
                enabled = !viewModel.answerSubmitted,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(option)

                    if (viewModel.answerSubmitted) {
                        Icon(
                            imageVector = if (isCorrect)
                                Icons.Default.Check
                            else
                                Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // NEXT BUTTON
        if (viewModel.answerSubmitted) {
            Button(
                onClick = {
                    val finished = viewModel.moveToNextQuestion()

                    if (finished) {
                        viewModel.finalizeQuiz()
                        navController.navigate("result") {
                            popUpTo("quiz") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Next")
            }
        }
    }
}