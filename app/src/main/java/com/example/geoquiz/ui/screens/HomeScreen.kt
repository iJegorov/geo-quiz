package com.example.geoquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
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
            text = "GeoQuiz",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Select Difficulty",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box {

            Button(onClick = {
                expanded = true
            }) {
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

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                navController.navigate("quiz")
            },

            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Quiz")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                navController.navigate("statistics")
            },

            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Statistics")
        }
    }
}