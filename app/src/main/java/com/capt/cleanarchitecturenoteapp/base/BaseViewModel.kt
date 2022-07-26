package com.capt.cleanarchitecturenoteapp.base

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel<UIEvent, UIAction> : ViewModel() {

    private var _eventFlow = MutableSharedFlow<UIAction>()
    val eventFlow = _eventFlow.asSharedFlow()

    override fun onCleared() {
        super.onCleared()
        Log.d(javaClass.name, "onCleared...")
    }

    abstract fun onEvent(event: UIEvent)

    protected suspend fun emitAction(uiEvent: UIAction) {
        _eventFlow.emit(uiEvent)
    }
}