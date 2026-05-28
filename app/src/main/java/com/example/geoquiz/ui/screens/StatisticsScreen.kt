package com.example.geoquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.geoquiz.viewmodel.QuizViewModel

@Composable
fun StatisticsScreen(
    navController: NavController,
    viewModel: QuizViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Statistics",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 📊 Quiz performance stats
        Text(
            text = "Performance",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Total Quizzes Played: ${viewModel.totalQuizzesPlayed}")
        Text("Total Correct Answers: ${viewModel.totalCorrectAnswers}")
        Text("Total Questions Answered: ${viewModel.totalQuestionsAnswered}")
        Text("Last Difficulty: ${viewModel.lastDifficultyPlayed}")

        Spacer(modifier = Modifier.height(24.dp))

        // 🎮 Gamification stats
        Text(
            text = "Progression",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Current Level: ${viewModel.currentLevel}")
        Text("Total Points: ${viewModel.totalPoints}")

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
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