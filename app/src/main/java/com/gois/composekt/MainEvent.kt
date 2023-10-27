package com.gois.composekt

sealed class MainEvent {
    class OnCurrentNameChanged(val value: String) : MainEvent()
    object OnAddButtonClicked : MainEvent()
    class OnNameSelected(val value: String) : MainEvent()
    object OnBottomSheetClosed : MainEvent()
}
