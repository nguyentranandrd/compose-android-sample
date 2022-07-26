package com.capt.cleanarchitecturenoteapp.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capt.cleanarchitecturenoteapp.base.BaseViewModel
import com.capt.cleanarchitecturenoteapp.ui.theme.Black30
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun StateDemoScreen() {
    var count by remember {
        mutableStateOf(0)
    }
    var text by remember {
        mutableStateOf("")
    }
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Demo state 1")
            Text(text = "Count: $count")
            Button(onClick = {
                count++
            }) {
                Text(text = "Click me")
            }
            OutlinedTextField(value = "", onValueChange = {}, label = {
                Text(text = "TextField doesn't have state")
            })
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = text, onValueChange = { text = it }, label = {
                Text(text = "TextField has state")
            })
        }
    }
}


@Preview
@Composable
fun StateDemoScreenPreview() {
    StateDemoScreen()
}


@Composable
fun StateDemoScreen2() {
    var a by remember {
        mutableStateOf(0)
    }

    var b by remember {
        mutableStateOf(0)
    }

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Demo state 2")

            Text(text = "a = $a")
            Button(onClick = {
                a++
            }) {
                Text(text = "Click a")
            }
            Text(text = "b = $b")
            Button(onClick = {
                b++
            }) {
                Text(text = "Click b")
            }

            Text(text = "sum = ${a + b}")
        }
    }
}


@Preview
@Composable
fun StateDemoScreen2Preview() {
    StateDemoScreen2()
}

@Composable
fun StateDemoScreen3() {

    val sumState = rememberSumState()

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Demo state 3")

            Text(text = "a = ${sumState.a.value}")
            Button(onClick = { sumState.inCreaseA() }) {
                Text(text = "Click a")
            }
            Text(text = "b = ${sumState.b.value}")
            Button(
                onClick = { sumState.inCreaseB() }
            ) {
                Text(text = "Click b")
            }

            Text(text = "sum = ${sumState.calSum()}")
        }
    }
}

class SumState(
    val a: MutableState<Int> = mutableStateOf(0),
    val b: MutableState<Int>
) {
    fun calSum() = a.value + b.value

    fun inCreaseA() {
        a.value++
    }

    fun inCreaseB() {
        b.value++
    }
}

@Composable
fun rememberSumState(
    a: MutableState<Int> = mutableStateOf(0),
    b: MutableState<Int> = mutableStateOf(0)
) = remember(a, b) {
    SumState(a, b)
}

@Preview
@Composable
fun StateDemoScreen3Preview() {
    StateDemoScreen3()
}


@Composable
fun StateDemoScreen4(
    viewModel: StateDemoViewModel = viewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    val state = viewModel.sumUiState

    LaunchedEffect(viewModel, scaffoldState) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is StateDemoViewModel.UIAction.CalcSumCompleted -> {
                    scaffoldState.snackbarHostState.showSnackbar(it.sum.toString(), "Dismiss")
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Demo state 4")

            Text(text = "a = ${state.a}")
            Button(onClick = {
                viewModel.onEvent(StateDemoViewModel.UIEvent.UpdateA(state.a + 1))
            }) {
                Text(text = "Click a")
            }
            Text(text = "b = ${state.b}")
            Button(onClick = {
                viewModel.onEvent(StateDemoViewModel.UIEvent.UpdateB(state.b + 1))
            }) {
                Text(text = "Click b")
            }

            Button(onClick = {
                viewModel.onEvent(StateDemoViewModel.UIEvent.CalcSum)
            }, enabled = !state.isLoading) {
                Text(text = "Sum")
            }
        }
        ProgressLoading(modifier = Modifier.fillMaxSize(), isLoading = state.isLoading)

    }
}

@Composable
fun ProgressLoading(isLoading: Boolean = true, modifier: Modifier) {
    if (isLoading) {
        Box(
            modifier = modifier
                .background(Black30)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }
    }
}


data class SumUIState(val a: Int = 0, val b: Int = 0, val isLoading: Boolean = false) {
    fun calcSum() = a + b
}


class StateDemoViewModel :
    BaseViewModel<StateDemoViewModel.UIEvent, StateDemoViewModel.UIAction>() {

    private var _sumUiState by mutableStateOf(SumUIState())
    val sumUiState: SumUIState
        get() = _sumUiState


    sealed class UIEvent {
        data class UpdateA(val newValue: Int) : UIEvent()
        data class UpdateB(val newValue: Int) : UIEvent()
        object CalcSum : UIEvent()
    }

    sealed class UIAction {
        data class CalcSumCompleted(val sum: Int) : UIAction()
    }

    override fun onEvent(event: UIEvent) {
        when (event) {
            is UIEvent.UpdateA -> {
                _sumUiState = sumUiState.copy(a = event.newValue)
            }
            is UIEvent.UpdateB -> {
                _sumUiState = sumUiState.copy(b = event.newValue)
            }
            is UIEvent.CalcSum -> {
                viewModelScope.launch {
                    _sumUiState = sumUiState.copy(isLoading = true)
                    delay(2000)
                    emitAction(UIAction.CalcSumCompleted(_sumUiState.calcSum()))
                    _sumUiState = sumUiState.copy(isLoading = false)
                }
            }
        }
    }
}

@Preview
@Composable
fun StateDemoScreen4Preview() {
    StateDemoScreen4()
}
