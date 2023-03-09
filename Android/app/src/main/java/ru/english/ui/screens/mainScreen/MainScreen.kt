package ru.english.ui.screens.mainScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.english.R
import ru.english.data.user.UserDataStore
import ru.english.data.user.model.User
import ru.english.data.user.model.UserRole
import ru.english.data.utils.UtilsDataStore
import ru.english.data.utils.model.Utils
import ru.english.data.withdrawalRequest.WithdrawalRequestDataStore
import ru.english.data.withdrawalRequest.model.WithdrawalRequest
import ru.english.ui.navigation.Screen
import ru.english.ui.screens.mainScreen.view.RewardAlertDialog
import ru.english.ui.theme.primaryText
import ru.english.ui.theme.tintColor
import ru.english.ui.view.BaseLottieAnimation
import ru.english.ui.view.LottieAnimationType

@Composable
fun MainScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val userDataStore = remember(::UserDataStore)
    val utilsDataStore = remember(::UtilsDataStore)
    val withdrawalRequestDataStore = remember(::WithdrawalRequestDataStore)
    var user by remember { mutableStateOf<User?>(null) }
    var utils by remember { mutableStateOf<Utils?>(null) }
    var rewardAlertDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit, block = {
        userDataStore.get { user = it }
        utilsDataStore.get({ utils = it })
    })

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

    if(rewardAlertDialog){
        RewardAlertDialog(
            utils = utils,
            countInterstitialAds = user?.countInterstitialAds ?: 0,
            countInterstitialAdsClick = user?.countInterstitialAdsClick ?: 0,
            countRewardedAds = user?.countRewardedAds ?: 0,
            countRewardedAdsClick = user?.countRewardedAdsClick ?: 0,
            onDismissRequest = { rewardAlertDialog = false },
            onSendWithdrawalRequest = { phoneNumber ->
                user ?: return@RewardAlertDialog

                val withdrawalRequest = WithdrawalRequest(
                    countInterstitialAds = user!!.countInterstitialAds,
                    countInterstitialAdsClick = user!!.countInterstitialAdsClick,
                    countRewardedAds = user!!.countRewardedAds,
                    countRewardedAdsClick = user!!.countRewardedAdsClick,
                    phoneNumber = phoneNumber,
                    userEmail = user!!.email,
                    version = 1
                )

                withdrawalRequestDataStore.create(withdrawalRequest) {
                    if (it.isSuccessful) {
                        rewardAlertDialog = false
                        Toast.makeText(context, "Заявка отправлена", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Ошибка: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    Column {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.size(
                width = screenWidthDp.dp,
                height = screenHeightDp.dp
            )
        ) {
            item {
                BaseLottieAnimation(
                    type = LottieAnimationType.LANGUAGE,
                    modifier = Modifier
                        .size(400.dp)
                        .padding(5.dp)
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
                        navController.navigate(Screen.WordsList.route)
                    }
                ) {
                    Text(text = "Словарь", color = primaryText)
                }

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
                        navController.navigate(Screen.Questions.route)
                    }
                ) {
                    Text(text = "Вопросы", color = primaryText)
                }

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
                        rewardAlertDialog = true
                    }
                ) {
                    Text(text = "Награда", color = primaryText)
                }

                if(user?.userRole == UserRole.ADMIN){
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
                            navController.navigate(Screen.Admin.route)
                        }
                    ) {
                        Text(text = "Админ", color = primaryText)
                    }
                }
            }
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4479af)))
    }
}