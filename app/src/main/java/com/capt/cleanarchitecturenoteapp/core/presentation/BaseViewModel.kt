package com.capt.cleanarchitecturenoteapp.core.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel<VMEvent, UIEvent> : ViewModel() {

    private var _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    override fun onCleared() {
        super.onCleared()
        Log.d(javaClass.name, "onCleared...")
    }

    abstract fun onEvent(event: VMEvent)

    protected suspend fun emitUIEvent(uiEvent: UIEvent) {
        _eventFlow.emit(uiEvent)
    }
}