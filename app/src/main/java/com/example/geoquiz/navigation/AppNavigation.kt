package com.example.geoquiz.navigation

import android.app.Application
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.geoquiz.data.repository.QuestionRepository
import com.example.geoquiz.ui.screens.*
import com.example.geoquiz.viewmodel.QuizViewModel

/**
 * AppNavigation
 *
 * Central navigation graph for the GeoQuiz application.
 *
 * Responsibilities:
 * - Defines all app screens and routes
 * - Creates and provides shared ViewModel instance
 * - Injects dependencies manually
 *
 * Architecture role:
 * - Acts as the "composition root" of the app
 * - Ensures a single QuizViewModel instance is shared across screens
 */
@Composable
fun AppNavigation() {

    // ---------------------------------------------------------
    // NAVIGATION CONTROLLER
    // ---------------------------------------------------------
    val navController = rememberNavController()
    // Context is required for repository and ViewModel creation
    val context = LocalContext.current

    // ---------------------------------------------------------
    // DEPENDENCY INJECTION (manual DI)
    // ---------------------------------------------------------
    /**
     * QuizEngine implementation (QuestionRepository)
     *
     * - Provides question data
     * - Handles adaptive learning (weights per question)
     * - Acts as data layer abstraction for ViewModel
     */
    val quizEngine = remember {
        QuestionRepository(context)
    }

    /**
     * Shared QuizViewModel instance
     *
     * - Survives across navigation destinations
     * - Maintains quiz state globally
     * - Receives QuizEngine via constructor injection
     */
    val quizViewModel = remember {
        QuizViewModel(
            application = context.applicationContext as Application,
            quizEngine = quizEngine
        )
    }



    // ---------------------------------------------------------
    // NAVIGATION GRAPH
    // ---------------------------------------------------------

    NavHost(navController = navController, startDestination = "home") {

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