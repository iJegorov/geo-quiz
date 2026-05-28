package com.example.geoquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geoquiz.viewmodel.QuizViewModel

@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: QuizViewModel
) {

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text(text = "Quiz Finished!")
        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Score: ${viewModel.score} / ${viewModel.getTotalQuestions()}")

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Quiz points earned: ${viewModel.currentQuizPoints}"
        )

        Text(
            text = "Total points: ${viewModel.totalPoints}"
        )

        Text(
            text = "Current level: ${viewModel.currentLevel}"
        )

        Button(
            onClick = {
                viewModel.resetQuiz()
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
        ) {
            Text("Back To Menu")
        }
    }
}