package com.example.geoquiz.navigation

import android.app.Application
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.geoquiz.data.repository.QuestionRepository
import com.example.geoquiz.ui.screens.*
import com.example.geoquiz.viewmodel.QuizViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current

    // ✅ FIX 1: create REAL instance
    val quizEngine = remember {
        QuestionRepository(context)
    }

    // ✅ FIX 2: pass correct dependency
    val quizViewModel = remember {
        QuizViewModel(
            application = context.applicationContext as Application,
            quizEngine = quizEngine
        )
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(navController, quizViewModel)
        }

        composable("quiz") {
            QuizScreen(navController, quizViewModel)
        }

        composable("result") {
            ResultScreen(navController, quizViewModel)
        }

        composable("statistics") {
            StatisticsScreen(navController, quizViewModel)
        }
    }
}