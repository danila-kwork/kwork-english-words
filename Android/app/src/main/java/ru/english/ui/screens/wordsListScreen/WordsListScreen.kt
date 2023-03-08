package ru.english.ui.screens.wordsListScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.rememberFlipController
import ru.english.data.words.WordsDataStore
import ru.english.data.words.model.Word
import ru.english.ui.theme.primaryText
import ru.english.ui.theme.secondaryBackground

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WordsListScreen(
    navController: NavController
) {
    val flipController = rememberFlipController()
    val wordsDataStore = remember(::WordsDataStore)
    var words by remember { mutableStateOf(emptyList<Word>()) }

    LaunchedEffect(key1 = Unit, block = {
        wordsDataStore.getWordsList { words = it }
    })

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            count = words.size,
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