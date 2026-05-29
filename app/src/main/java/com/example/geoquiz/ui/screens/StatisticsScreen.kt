package com.example.geoquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.geoquiz.viewmodel.QuizViewModel
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding

@Composable
fun StatisticsScreen(
    navController: NavController,
    viewModel: QuizViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .navigationBarsPadding()
    ) {

        Text(
            text = "📊 Statistics",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        StatCard(
            title = "Performance",
            stats = listOf(
                "Quizzes" to viewModel.totalQuizzesPlayed.toString(),
                "Correct" to viewModel.totalCorrectAnswers.toString(),
                "Accuracy" to "${if (viewModel.totalQuestionsAnswered > 0)
                    (viewModel.totalCorrectAnswers * 100 / viewModel.totalQuestionsAnswered)
                else 0}%"
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        StatCard(
            title = "Progress",
            stats = listOf(
                "Level" to viewModel.currentLevel,
                "Points" to viewModel.totalPoints.toString(),
                "Difficulty" to viewModel.lastDifficultyPlayed
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
fun StatCard(title: String, stats: List<Pair<String, String>>) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text(title, style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(12.dp))

            stats.forEach {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(it.first)
                    Text(it.second, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}