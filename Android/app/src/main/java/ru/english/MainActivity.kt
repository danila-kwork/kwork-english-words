package ru.english

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import ru.english.ui.navigation.Screen
import ru.english.ui.screens.authScreen.AuthScreen
import ru.english.ui.screens.mainScreen.MainScreen
import ru.english.ui.screens.questionsScreen.QuestionsScreen
import ru.english.ui.screens.wordsListScreen.WordsListScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val auth = remember { FirebaseAuth.getInstance() }

            NavHost(
                navController = navController,
                startDestination = if(auth.currentUser == null)
                    Screen.Auth.route
                else
                    Screen.Main.route,
                builder = {
                    composable(Screen.Auth.route){
                        AuthScreen(navController = navController)
                    }
                    composable(Screen.Main.route){
                        MainScreen(navController = navController)
                    }
                    composable(Screen.WordsList.route){
                        WordsListScreen(navController = navController)
                    }
                    composable(Screen.Questions.route){
                        QuestionsScreen(navController = navController)
                    }
                }
            )
        }
    }

    fun fullScreencall() {
        //for new api versions.
        val decorView = window.decorView
        val uiOptions: Int =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions
    }
}