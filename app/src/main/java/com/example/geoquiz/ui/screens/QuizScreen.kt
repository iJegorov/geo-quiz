package com.example.geoquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geoquiz.viewmodel.QuizViewModel
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment


@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel
) {

    val question = viewModel.currentQuestion

    Column {

        Text(text = question.questionText)
        Text(text = "Current score: ${viewModel.score}")

        Text(
            text = "Question ${viewModel.currentQuestionIndex + 1} / ${viewModel.getTotalQuestions()}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        question.options.forEach { option ->
            Button(onClick = {

                val isFinished = viewModel.answerQuestion(option)

                if (isFinished) {
                    navController.navigate("result") {
                        popUpTo("quiz") { inclusive = true }
                    }
                }

            }) {
                Text(option)
            }
        }
    }
}