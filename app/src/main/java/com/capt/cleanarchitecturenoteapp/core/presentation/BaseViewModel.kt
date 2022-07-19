package com.capt.cleanarchitecturenoteapp.core.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel<UIEvent, UIState> : ViewModel() {

    private var _eventFlow = MutableSharedFlow<UIState>()
    val eventFlow = _eventFlow.asSharedFlow()

    override fun onCleared() {
        super.onCleared()
        Log.d(javaClass.name, "onCleared...")
    }

    abstract fun onEvent(event: UIEvent)

    protected suspend fun emitUIEvent(uiEvent: UIState) {
        _eventFlow.emit(uiEvent)
    }
}