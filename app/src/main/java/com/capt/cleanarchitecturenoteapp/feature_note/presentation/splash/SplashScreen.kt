package com.capt.cleanarchitecturenoteapp.feature_note.presentation.splash

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.capt.cleanarchitecturenoteapp.feature_note.presentation.util.Screen
import kotlinx.coroutines.delay


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashScreen(navController: NavController) {


    LaunchedEffect(true) {
        var count = 0
        repeat(2) {
            if (count < 2) {
                delay(1000L)
                count++
            } else
                return@repeat
        }
        navController.navigate(Screen.NotesScreen.route) {
            popUpTo(Screen.SplashScreen.route) {
                inclusive = true
            }
        }
    }

    Scaffold {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Note App", fontSize = 36.sp)
//            Box(Modifier.background(color = Color.Green)) {
//                Text(
//                    text = "Hello",
//                    Modifier
//                        .padding(20.dp)
//                        .shadow(10.dp, RoundedCornerShape(10.dp))
//                        .clip(RoundedCornerShape(10.dp))
//                        .background(color = Color.Red)
//                        .padding(5.dp)
//                )
//            }

        }
    }
}