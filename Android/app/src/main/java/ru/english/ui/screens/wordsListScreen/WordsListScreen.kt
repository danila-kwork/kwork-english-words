package ru.english.ui.screens.wordsListScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.rememberFlipController
import org.joda.time.Period
import ru.english.R
import ru.english.data.user.UserDataStore
import ru.english.data.user.model.User
import ru.english.data.user.model.userSumMoneyVersion2
import ru.english.data.utils.UtilsDataStore
import ru.english.data.utils.model.Utils
import ru.english.data.words.WordsDataStore
import ru.english.data.words.model.Word
import ru.english.data.words.model.WordsLevel
import ru.english.ui.theme.primaryText
import ru.english.ui.theme.secondaryBackground
import ru.english.ui.view.Board
import ru.english.yandexAds.RewardedYandexAds

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
    })

    LaunchedEffect(key1 = wordsLevel, block = {
        wordsDataStore.getWordsList(wordsLevel) { words = it }
    })

    LaunchedEffect(key1 = pagerState.currentPage, block = {
        if(pagerState.currentPage % 3 == 0 && pagerState.currentPage != 0){
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
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

            Column {
                Card(
                    modifier = Modifier.padding(5.dp),
                    shape = AbsoluteRoundedCornerShape(10.dp),
                    backgroundColor = secondaryBackground
                ) {
                    TextButton(
                        modifier = Modifier.padding(5.dp),
                        onClick = { changeWordsLevelDropdownMenu = true }
                    ) {
                        Text(text = wordsLevel.text, color = primaryText)
                    }
                }
                DropdownMenu(
                    expanded = changeWordsLevelDropdownMenu,
                    onDismissRequest = { changeWordsLevelDropdownMenu = false }
                ) {
                    WordsLevel.values().forEach {
                        DropdownMenuItem(onClick = { wordsLevel = it; changeWordsLevelDropdownMenu = false }) {
                            Text(
                                text = it.text,
                                color = primaryText
                            )
                        }
                    }
                }
            }
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
                                Text(text = word.wordEn, color = primaryText)
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
                                Text(text = word.wordRu, color = primaryText)
                            }
                        }
                    }
                )
            }
        }
    }
}