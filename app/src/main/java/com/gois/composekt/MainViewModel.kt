package com.gois.composekt

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    val names = mutableStateListOf(*initialNames.toTypedArray())

    private val _currentName: MutableStateFlow<String> = MutableStateFlow("")
    val currentName = _currentName.asStateFlow()

    private val _selectedName: MutableStateFlow<String?> = MutableStateFlow(null)
    val selectedName = _selectedName.asStateFlow()

    fun onEvent(event: MainEvent) {
        when (event) {
            MainEvent.OnAddButtonClicked -> addNewName()
            is MainEvent.OnCurrentNameChanged -> onCurrentNameChanged(event.value)
            is MainEvent.OnNameSelected -> onNameSelected(event.value)
            MainEvent.OnBottomSheetClosed -> onBottomSheetClosed()
        }
    }

    private fun addNewName() {
        val newName = currentName.value
        if (newName.isNotBlank()) {
            names.add(newName)
            _currentName.value = ""
        }
    }

    private fun onCurrentNameChanged(value: String) {
        _currentName.value = value
    }

    private fun onNameSelected(value: String) {
        _selectedName.value = value
    }

    private fun onBottomSheetClosed() {
        _selectedName.value = null
    }

    companion object {
        val initialNames = listOf(
            "Amadeus",
            "Casimiro",
            "Perséfone",
            "Apolo",
            "Amélia",
            "Caliana",
            "Luna",
            "Benjamin"
        )
    }
}