package com.example.geoquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.geoquiz.viewmodel.QuizViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: QuizViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "🌍 GeoQuiz",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Challenge your geography knowledge",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Progress Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text("Your Progress", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Level: ${viewModel.currentLevel}", style = MaterialTheme.typography.headlineSmall)
                Text("Points: ${viewModel.totalPoints}", style = MaterialTheme.typography.titleMedium)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Difficulty
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text("Select Difficulty", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(12.dp))

                Box {

                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(viewModel.selectedDifficulty)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("Easy", "Medium", "Hard").forEach { difficulty ->
                            DropdownMenuItem(
                                text = { Text(difficulty) },
                                onClick = {
                                    viewModel.setDifficulty(difficulty)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("quiz") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Start Quiz")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { navController.navigate("statistics") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Statistics")
        }
    }
}