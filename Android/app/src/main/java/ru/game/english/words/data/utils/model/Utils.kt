package ru.game.english.words.data.utils.model

import com.google.firebase.database.DataSnapshot

data class Utils(
    val banner_yandex_ads_id: String,
    val clicker:Boolean = false,
    val interstitial_ads_click_price: Double,
    val interstitial_ads_price: Double,
    val interstitial_yandex_ads_id: String,
    val min_price_withdrawal_request: Double,
    val rewarded_ads_click: Double,
    val rewarded_ads_price: Double,
    val banner_ads_click_price: Double,
    val banner_ads_price: Double,
    val rewarded_yandex_ads_id: String,
    val words_count: Int
)

fun DataSnapshot.mapUtils(): Utils {
    return Utils(
        banner_yandex_ads_id = child("banner_yandex_ads_id").value.toString(),
        clicker = child("clicker").value.toString().toBoolean(),
        interstitial_ads_click_price = child("interstitial_ads_click_price").value.toString().toDouble(),
        interstitial_ads_price = child("interstitial_ads_price").value.toString().toDouble(),
        interstitial_yandex_ads_id = child("interstitial_yandex_ads_id").value.toString(),
        min_price_withdrawal_request = child("min_price_withdrawal_request").value.toString().toDouble(),
        rewarded_ads_click = child("rewarded_ads_click").value.toString().toDouble(),
        rewarded_ads_price = child("rewarded_ads_price").value.toString().toDouble(),
        rewarded_yandex_ads_id = child("rewarded_yandex_ads_id").value.toString(),
        banner_ads_price = child("banner_ads_price").value.toString().toDouble(),
        banner_ads_click_price = child("banner_ads_click_price").value.toString().toDouble(),
        words_count = child("words_count").value.toString().toInt(),
    )
}