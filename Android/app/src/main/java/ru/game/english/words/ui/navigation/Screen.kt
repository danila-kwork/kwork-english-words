package ru.game.english.words.ui.navigation

sealed class Screen(val route: String) {
    object Main: Screen("main_screen")
    object Auth: Screen("auth_screen")
    object WordsList: Screen("words_list_screen")
    object Questions: Screen("questions_screen")
    object Admin: Screen("admin_screen")
    object Settings: Screen("settings_screen")
    object WithdrawalRequests: Screen("withdrawal_requests_screen")
    object AddWords: Screen("add_words_screen")
    object Ads: Screen("ads_screen")
}