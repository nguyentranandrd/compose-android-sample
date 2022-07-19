package com.capt.cleanarchitecturenoteapp.feature_note.presentation.splash

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.capt.cleanarchitecturenoteapp.feature_note.presentation.util.Screen
import kotlinx.coroutines.delay

var i = 0
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashScreen(navController: NavController) {
    var count by remember {
        mutableStateOf(0)
    }
    var count1 by remember {
        mutableStateOf(0)
    }
    var count2 by remember {
        mutableStateOf(0)
    }


//    var scope = rememberCoroutineScope()

//    Log.d("TAG", "navigate??")
//    if (count == 5) {
//        scope.launch {
//            Log.d("TAG", "navigating")
//            navController.navigate(Screen.NotesScreen.route)
//        }
//    }

    Log.d("TAG", "count 1: $count1")
    Log.d("TAG", "count 2: $count2")

//    SideEffect {
//        i ++
//        Log.d("TAG", "i= $i")
//    }
    LaunchedEffect(true) {
        Log.d("TAG", "First launch")
        repeat(5) {
            if (count < 5) {
                delay(1000L)
                count++
            } else
                return@repeat
        }
    }

    LaunchedEffect(key1 = count) {
            Log.d("TAG", "navigate??")
        if (count == 5) {
            navController.navigate(Screen.NotesScreen.route)
        }
    }

    Scaffold {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Note App")
            Text(text = "$count")
            Text(text = "$count1", Modifier.clickable {
                count1++
            })
            Text(text = "$count2", Modifier.clickable {
                count2++
            })

        }
    }
}