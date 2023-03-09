package ru.english.ui.navigation

sealed class Screen(val route: String) {
    object Main: Screen("main_screen")
    object Auth: Screen("auth_screen")
    object WordsList: Screen("words_list_screen")
    object Questions: Screen("questions_screen")
}