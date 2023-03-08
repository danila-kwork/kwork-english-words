package ru.english.ui.navigation

sealed class Screen(val route: String) {
    object Main: Screen("main_screen")
    object Auth: Screen("auth_screen")
    object WordsList: Screen("Words_list_screen")
}