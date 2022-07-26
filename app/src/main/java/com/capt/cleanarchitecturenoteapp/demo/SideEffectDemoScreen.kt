package com.capt.cleanarchitecturenoteapp.demo

import android.media.Image
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun SideEffectDemoScreen(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    var a by remember {
        mutableStateOf(0)
    }

    var b by remember {
        mutableStateOf(0)
    }

    var c by remember {
        mutableStateOf(0)
    }

    var isVisible by remember {
        mutableStateOf(true)
    }


    LaunchedEffect(key1 = a, key2 = b) {
        scaffoldState.snackbarHostState.showSnackbar("${a + b}", "Dismiss")
    }


    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Side Effect demo", fontSize = 30.sp)
            Spacer(modifier = Modifier.height(100.dp))
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { a++ }) {
                    Text(text = "Click me")
                }
                Text(text = "a = $a", fontSize = 24.sp)
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { b++ }) {
                    Text(text = "Click me")
                }
                Text(text = "b = $b", fontSize = 24.sp)
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { c++ }) {
                    Text(text = "Click me")
                }
                Text(text = "c = $c", fontSize = 24.sp)
            }
            Button(onClick = {
                isVisible = !isVisible
            }) {
                Text(text = if (isVisible) "Hide" else "Show")
            }
            if (isVisible) {
                Count {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("on Dispose: $it", "Dismiss")
                    }
                }
            }
            UpdatedStateComposable(value = "a = $a")
        }
    }
}

@Composable
fun Count(onDispose: (Int) -> Unit) {
    var count by rememberSaveable {
        mutableStateOf(0)
    }

    //cleaned up after the keys change or if the composable leaves the Composition
    DisposableEffect(true) {
        onDispose {
            onDispose(count)
        }
    }

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(onClick = { count++ }) {
            Text(text = "Click me")
        }
        Text(text = "c = $count", fontSize = 24.sp)
    }
}

@Composable
fun UpdatedStateComposable(value: String) {
    val updatedState by rememberUpdatedState(newValue = value)

    val normalState by remember {
        mutableStateOf(value)
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Normal state: $normalState", fontSize = 24.sp)
        Text(text = "Updated state: $updatedState", fontSize = 24.sp)
    }
}

@Preview
@Composable
fun SideEffectDemoScreenPreview() {
    SideEffectDemoScreen()
}


var i = 0
@Composable
fun SideEffectDemo(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    var a by remember {
        mutableStateOf(0)
    }
    var b by remember {
        mutableStateOf(0)
    }
    rememberUsingA(a = a)
//    LaunchedEffect(key1 = a) {
//        scaffoldState.snackbarHostState.showSnackbar("$a", "Dismiss")
//    }
//    b
//    i++
//    Log.d("TAG", "i=$i")

//    SideEffect {
//        i++
////        b
//        Log.d("TAG", "i=$i")
//    }

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Side Effect demo", fontSize = 30.sp)
            Spacer(modifier = Modifier.height(100.dp))
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { a++ }) {
                    Text(text = "Click me")
                }
                Text(text = "a = $a", fontSize = 24.sp)
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { b++ }) {
                    Text(text = "Click me")
                }
                Text(text = "b = $b", fontSize = 24.sp)
            }

        }
    }

}

@Composable
fun rememberUsingA(a: Int) {
    SideEffect {
        Log.d("TAG", "rememberA: $a")
    }
}

@Preview
@Composable
fun SideEffectPreview() {
    SideEffectDemo()
}

