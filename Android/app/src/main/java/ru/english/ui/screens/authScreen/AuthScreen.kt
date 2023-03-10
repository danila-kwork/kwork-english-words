package ru.english.ui.screens.authScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.english.R
import ru.english.data.auth.AuthDataStore
import ru.english.data.utils.UtilsDataStore
import ru.english.data.utils.model.Utils
import ru.english.ui.navigation.Screen
import ru.english.ui.theme.primaryText
import ru.english.ui.theme.secondaryBackground
import ru.english.ui.theme.tintColor
import ru.english.ui.view.BaseLottieAnimation
import ru.english.ui.view.LottieAnimationType

@Composable
fun AuthScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    var utils by remember { mutableStateOf<Utils?>(null) }
    val utilsDataStore = remember(::UtilsDataStore)
    val authDataStore = remember(::AuthDataStore)

    LaunchedEffect(key1 = Unit, block = {
        utilsDataStore.get(onSuccess = {
            utils = it
        })
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

    Column {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(50.dp))

                BaseLottieAnimation(
                    type = LottieAnimationType.WELCOME,
                    modifier = Modifier
                        .size(330.dp)
                        .padding(5.dp)
                )

                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(5.dp).fillMaxWidth(),
                    fontWeight = FontWeight.W900,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )

                OutlinedTextField(
                    modifier = Modifier.padding(5.dp),
                    value = email,
                    onValueChange = { email = it },
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
                        Text(text = "?????????????????????? ??????????", color = primaryText)
                    }
                )

                OutlinedTextField(
                    modifier = Modifier.padding(5.dp),
                    value = password,
                    onValueChange = { password = it },
                    shape = AbsoluteRoundedCornerShape(15.dp),
                    visualTransformation = PasswordVisualTransformation(),
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
                        Text(text = "??????????????", color = primaryText)
                    }
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
                        try {
                            authDataStore.signIn(email.trim(),password.trim(),{
                                navController.navigate(Screen.Main.route)
                            },{
                                error = it
                            })
                        }catch(e: IllegalArgumentException){
                            error = "?????????????????? ?????? ????????"
                        }catch (e:Exception){
                            error = "????????????"
                        }
                    }
                ) {
                    Text(text = "????????????????????????????????", color = primaryText)
                }

                Button(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = AbsoluteRoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = tintColor
                    ),
                    onClick = {
                        try {
                            authDataStore.registration(email,password,{
                                navController.navigate(Screen.Main.route)
                            },{
                                error = it
                            })
                        }catch(e: IllegalArgumentException){
                            error = "?????????????????? ?????? ????????"
                        }catch (e:Exception){
                            error = "????????????"
                        }
                    }
                ) {
                    Text(text = "????????????????????????????????????", color = primaryText)
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF4479af)))
    }
}