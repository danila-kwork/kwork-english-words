package ru.game.english.words.common

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openBrowser(url: String) {

    var validUrl = url

    if (!validUrl.startsWith("http://") && !validUrl.startsWith("https://"))
        validUrl = "https://$validUrl";

    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
    this.startActivity(browserIntent)
}