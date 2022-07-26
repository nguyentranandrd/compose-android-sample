package com.capt.cleanarchitecturenoteapp.feature_note.presentation.add_notes

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.capt.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.capt.cleanarchitecturenoteapp.feature_note.presentation.add_notes.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {


    val noteUiState = viewModel.noteUiState

    val scaffoldState = rememberScaffoldState()

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteUiState.color)
        )
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(scaffoldState.snackbarHostState) {
        // like useEffect(()=> {
        //
        // }, [scaffoldState.snackBarHostState])
        viewModel.eventFlow.collectLatest {
            when (it) {
                is AddEditNoteViewModel.UIAction.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = it.message)
                }
                is AddEditNoteViewModel.UIAction.BackToNotesScreen -> {
                    navController.navigateUp()
                }
            }
        }
    }
    Log.d("TAG", "Out...")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditNoteViewModel.UIEvent.SaveNote)
            }, backgroundColor = MaterialTheme.colors.primary) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Save Note")
            }
        }, scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.noteColors.forEach {
                    val colorInt = it.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(it)
                            .border(
                                width = 3.dp,
                                color = if (noteUiState.color == colorInt) Color.Black else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(durationMillis = 500)
                                    )
                                }
                                viewModel.onEvent(AddEditNoteViewModel.UIEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = noteUiState.title.text,
                hint = noteUiState.title.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteViewModel.UIEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteViewModel.UIEvent.ChangeTitleFocus(it))
                },
                isHintVisible = noteUiState.title.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = noteUiState.content.text,
                hint = noteUiState.content.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteViewModel.UIEvent.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteViewModel.UIEvent.ChangeContentFocus(it))
                },
                isHintVisible = noteUiState.content.isHintVisible,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxHeight()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}
