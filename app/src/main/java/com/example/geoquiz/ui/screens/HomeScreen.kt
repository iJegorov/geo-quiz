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



/**
 * HomeScreen
 * - Displays user progress (level and total points)
 * - Allows difficulty selection (Easy / Medium / Hard)
 * - Starts quiz navigation
 * - Navigates to statistics screen
 *
 * State source:
 * - Shared QuizViewModel (single source of truth)
 */
@Composable
fun HomeScreen(navController: NavController, viewModel: QuizViewModel) {


    // Controls dropdown menu visibility for difficulty selection
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ---------------------------------------------------------
        // APP TITLE
        // ---------------------------------------------------------
        Text(
            text = "🌍 GeoQuiz",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle description
        Text(
            text = "Challenge your geography knowledge",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(40.dp))


        // ---------------------------------------------------------
        // PROGRESS SECTION
        // ---------------------------------------------------------
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text("Your Progress", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Level: ${viewModel.currentLevel}",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "Points: ${viewModel.totalPoints}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        // ---------------------------------------------------------
        // DIFFICULTY SELECTION SECTION
        // ---------------------------------------------------------
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



        // ---------------------------------------------------------
        // START QUIZ BUTTON
        // ---------------------------------------------------------
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



        // ---------------------------------------------------------
        // NAVIGATE TO STATISTICS
        // ---------------------------------------------------------
        OutlinedButton(
            onClick = { navController.navigate("statistics") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Statistics")
        }
    }
}