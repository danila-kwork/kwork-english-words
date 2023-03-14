package ru.game.english.words.ui.view

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*
import ru.game.english.words.R

enum class LottieAnimationType(@RawRes val resId: Int) {
    WELCOME(R.raw.welcome),
    LOGIN(R.raw.login),
    LANGUAGE(R.raw.language)
}


@Composable
fun BaseLottieAnimation(
    modifier: Modifier = Modifier,
    type:LottieAnimationType,
    iterations:Int = LottieConstants.IterateForever
){
    val compositionResult =
        rememberLottieComposition(spec = LottieCompositionSpec.RawRes(type.resId))

    Animation(
        modifier = modifier,
        iterations = iterations,
        compositionResult = compositionResult
    )
}

@Composable
private fun Animation(
    modifier: Modifier = Modifier,
    iterations:Int = LottieConstants.IterateForever,
    compositionResult: LottieCompositionResult
){
    val progress = animateLottieCompositionAsState(
        composition = compositionResult.value,
        iterations = iterations,
    )

    LottieAnimation(
        composition = compositionResult.value,
        progress = progress.progress,
        modifier = modifier
    )
}