package ru.game.english.words.ui.screens.wordsListScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.rememberFlipController
import org.joda.time.Period
import ru.game.english.words.R
import ru.game.english.words.data.user.UserDataStore
import ru.game.english.words.data.user.model.User
import ru.game.english.words.data.user.model.userSumMoneyVersion2
import ru.game.english.words.data.utils.UtilsDataStore
import ru.game.english.words.data.utils.model.Utils
import ru.game.english.words.data.words.WordsDataStore
import ru.game.english.words.data.words.model.Word
import ru.game.english.words.data.words.model.WordsLevel
import ru.game.english.words.ui.theme.primaryBackground
import ru.game.english.words.ui.theme.primaryText
import ru.game.english.words.ui.theme.secondaryBackground
import ru.game.english.words.ui.view.Board
import ru.game.english.words.yandexAds.RewardedYandexAds

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WordsListScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val flipController = rememberFlipController()
    val pagerState = rememberPagerState()
    val wordsDataStore = remember(::WordsDataStore)
    var words by remember { mutableStateOf(emptyList<Word>()) }
    var user by remember { mutableStateOf<User?>(null) }
    val userDataStore = remember(::UserDataStore)
    val utilsDataStore = remember(::UtilsDataStore)
    var utils by remember { mutableStateOf<Utils?>(null) }
    var wordsLevel by remember { mutableStateOf(WordsLevel.FIRST) }
    var changeWordsLevelDropdownMenu by remember { mutableStateOf(true) }

    val rewardedYandexAds = remember {
        RewardedYandexAds(context, onDismissed = { adClickedDate, returnedToDate, rewarded ->
            if(adClickedDate != null && returnedToDate != null && rewarded && user != null){
                if((Period(adClickedDate, returnedToDate)).seconds >= 10){
                    userDataStore.updateCountRewardedAdsClick(user!!.countRewardedAdsClick + 1)
                }else {
                    userDataStore.updateCountRewardedAds(user!!.countRewardedAds + 1)
                }
            } else if(rewarded && user != null){
                userDataStore.updateCountRewardedAds(user!!.countRewardedAds + 1)
            }
        })
    }

    LaunchedEffect(key1 = Unit, block = {
        userDataStore.get { user = it }
        utilsDataStore.get({ utils = it })
        changeWordsLevelDropdownMenu = true
    })

    LaunchedEffect(key1 = wordsLevel, block = {
        wordsDataStore.getWordsList(wordsLevel) { words = it }
    })

    LaunchedEffect(key1 = pagerState.currentPage, block = {
        if(pagerState.currentPage % 5 == 0 && pagerState.currentPage != 0){
            rewardedYandexAds.show()
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

        if(changeWordsLevelDropdownMenu){
            AlertDialog(
                backgroundColor = primaryBackground,
                shape = AbsoluteRoundedCornerShape(20.dp),
                onDismissRequest = { changeWordsLevelDropdownMenu = false },
                title = {
                    Text(
                        text = "Выберите уровень",
                        modifier = Modifier
                            .padding(vertical = 15.dp, horizontal = 2.dp)
                            .fillMaxWidth(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.W900,
                        textAlign = TextAlign.Center
                    )
                },
                buttons = {
                    Column(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        WordsLevel.values().forEach {
                            Divider(color = secondaryBackground)
                            DropdownMenuItem(
                                modifier = Modifier.background(primaryBackground),
                                onClick = { wordsLevel = it; changeWordsLevelDropdownMenu = false }
                            ) {
                                Text(
                                    text = it.text,
                                    color = primaryText,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(),
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
            HorizontalPager(
                count = words.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { index ->
                val word = words[index]

                Flippable(
                    modifier = Modifier.size(300.dp),
                    flipController = flipController,
                    frontSide = {
                        Card(
                            backgroundColor = secondaryBackground,
                            shape = AbsoluteRoundedCornerShape(15.dp),
                            modifier = Modifier.size(280.dp)
                        ){
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = word.wordEn,
                                    color = primaryText,
                                    fontWeight = FontWeight.W900,
                                    fontSize = 25.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    },
                    backSide = {
                        Card(
                            backgroundColor = secondaryBackground,
                            shape = AbsoluteRoundedCornerShape(15.dp),
                            modifier = Modifier.size(280.dp)
                        ){
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = word.wordRu,
                                    color = primaryText,
                                    fontWeight = FontWeight.W900,
                                    fontSize = 25.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}