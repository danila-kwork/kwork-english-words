package ru.english.ui.screens.questionsScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.joda.time.Period
import ru.english.R
import ru.english.data.user.UserDataStore
import ru.english.data.user.model.User
import ru.english.data.user.model.userSumMoneyVersion2
import ru.english.data.utils.UtilsDataStore
import ru.english.data.utils.model.Utils
import ru.english.data.words.WordsDataStore
import ru.english.data.words.model.Word
import ru.english.ui.theme.primaryText
import ru.english.ui.theme.secondaryBackground
import ru.english.ui.theme.tintColor
import ru.english.ui.view.Board
import ru.english.yandexAds.InterstitialYandexAds

@Composable
fun QuestionsScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val wordsDataStore = remember(::WordsDataStore)
    var user by remember { mutableStateOf<User?>(null) }
    val userDataStore = remember(::UserDataStore)
    val utilsDataStore = remember(::UtilsDataStore)
    var utils by remember { mutableStateOf<Utils?>(null) }
    var word by remember { mutableStateOf<Word?>(null) }
    var userAnswer by remember { mutableStateOf("") }
    val focusRequester = remember(::FocusRequester)
    var countQuestions by remember { mutableStateOf(0) }

    val interstitialYandexAds = remember {
        InterstitialYandexAds(context, onDismissed = { adClickedDate, returnedToDate ->
            user ?: return@InterstitialYandexAds

            if(adClickedDate != null && returnedToDate != null){
                if((Period(adClickedDate, returnedToDate)).seconds >= 10){
                    userDataStore.updateCountInterstitialAdsClick(user!!.countInterstitialAdsClick + 1)
                }else {
                    userDataStore.updateCountInterstitialAds(user!!.countInterstitialAds + 1)
                }
            } else {
                userDataStore.updateCountInterstitialAds(user!!.countInterstitialAds + 1)
            }
        })
    }

    LaunchedEffect(key1 = Unit, block = {
        userDataStore.get { user = it }
        utilsDataStore.get({ utils = it })
        wordsDataStore.getRandomWord { word = it; countQuestions++ }

        focusRequester.requestFocus()
    })

    LaunchedEffect(key1 = countQuestions, block = {
        if(countQuestions % 3 == 0){
            interstitialYandexAds.show()
        }
    })

    Surface(
        color = Color(0xFF4479af)
    ) {
        Image(
            bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.resources, R.drawable.main_background),
                screenWidthDp,
                screenHeightDp,
                false
            ).asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.size(
                width = screenWidthDp.dp,
                height = screenHeightDp.dp
            )
        )

        Column {
            Board(
                modifier = Modifier.padding(10.dp),
                text = userSumMoneyVersion2(
                    utils = utils,
                    countBannerAds = user?.countBannerAds ?: 0,
                    countBannerAdsClick = user?.countBannerAdsClick ?: 0,
                    countInterstitialAds = user?.countInterstitialAds ?: 0,
                    countInterstitialAdsClick = user?.countInterstitialAdsClick ?: 0,
                    countRewardedAds = user?.countRewardedAds ?: 0,
                    countRewardedAdsClick = user?.countRewardedAdsClick ?: 0
                ).toString(),
                width = (screenWidthDp / 2).toDouble(),
                height = (screenHeightDp / 10).toDouble()
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Как переводиться слово\n${word?.wordEn}",
                fontWeight = FontWeight.W900,
                modifier = Modifier.padding(5.dp),
                color = primaryText,
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                modifier = Modifier
                    .padding(5.dp)
                    .focusRequester(focusRequester),
                value = userAnswer,
                onValueChange = { userAnswer = it },
                shape = AbsoluteRoundedCornerShape(15.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = primaryText,
                    disabledTextColor = tintColor,
                    backgroundColor = secondaryBackground,
                    cursorColor = tintColor,
                    focusedBorderColor = tintColor,
                    unfocusedBorderColor = secondaryBackground,
                    disabledBorderColor = secondaryBackground
                ),
                label = {
                    Text(text = "Ответ", color = primaryText)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(onGo = {
                    if(userAnswer.lowercase().trim() == word?.wordRu?.lowercase()?.trim()){
                        wordsDataStore.getRandomWord { word = it; countQuestions++ }
                        userAnswer = ""
                        Toast.makeText(context, "Верно", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(context, "Не правильно", Toast.LENGTH_SHORT).show()
                    }
                })
            )

            Button(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = tintColor
                ),
                shape = AbsoluteRoundedCornerShape(15.dp),
                onClick = {
                    userAnswer = word?.wordRu.toString()
                }
            ) {
                Text(text = "Подсказка", color = primaryText)
            }
        }
    }
}