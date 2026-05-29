package com.example.geoquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.geoquiz.viewmodel.QuizViewModel

@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: QuizViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "🎉 Quiz Completed!",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "${viewModel.score} / ${viewModel.getTotalQuestions()}",
                    style = MaterialTheme.typography.displayMedium
                )

                Text("Correct Answers")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Points earned: +${viewModel.currentQuizPoints}")
        Text("Total points: ${viewModel.totalPoints}")

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.resetQuiz()
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}