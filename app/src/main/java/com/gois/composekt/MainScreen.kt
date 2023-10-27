package com.gois.composekt

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gois.composekt.ui.theme.ComposeKTTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val currentName by viewModel.currentName.collectAsStateWithLifecycle()
    val selectedName by viewModel.selectedName.collectAsStateWithLifecycle()

    MainScreen(
        currentName = currentName,
        names = viewModel.names,
        selectedName = selectedName,
        onEvent = { event -> viewModel.onEvent(event) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    currentName: String,
    names: List<String>,
    selectedName: String?,
    onEvent: (MainEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var isAlertDialogOpen by remember {
        mutableStateOf(false)
    }
    val lazyStateList = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "ComposeKT") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            AddNameSection(
                currentValue = currentName,
                onNameChanged = { value -> onEvent(MainEvent.OnCurrentNameChanged(value)) },
                onAddClicked = {
                    if (currentName.isNotBlank()) {
                        isAlertDialogOpen = true
                    }
                }
            )
            Spacer(Modifier.height(16.dp))
            NamesList(
                state = lazyStateList,
                names = names
            ) { clickedName ->
                onEvent(MainEvent.OnNameSelected(clickedName))
            }
        }
    }

    if (isAlertDialogOpen) {
        AlertDialog(
            onDismissRequest = { isAlertDialogOpen = false },
            confirmButton = {
                TextButton(onClick = {
                    isAlertDialogOpen = false
                    onEvent(MainEvent.OnAddButtonClicked)
                    focusManager.clearFocus()
                    scope.launch {
                        lazyStateList.animateScrollToItem(0)
                    }
                }) {
                    Text(text = "Add")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isAlertDialogOpen = false
                }) {
                    Text(text = "Cancel")
                }
            },
            title = { Text(text = "Add new name") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "You are adding \"$currentName\". Are you sure?"
                    )
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Info Button"
                )
            }
        )
    }

    ComposeBottomSheet(
        selectedName = selectedName,
        onDismiss = { onEvent(MainEvent.OnBottomSheetClosed) }
    )
}

@Composable
fun AddNameSection(
    currentValue: String,
    onNameChanged: (String) -> Unit,
    onAddClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("name_text_field"),
            value = currentValue,
            onValueChange = onNameChanged,
            label = {
                Text(text = "Insert a name")
            },
            shape = RoundedCornerShape(
                topStartPercent = 25,
                topEndPercent = 25,
                bottomEndPercent = 0,
                bottomStartPercent = 0
            )
        )
        Spacer(Modifier.height(16.dp))
        Button(
            modifier = Modifier.testTag("add_button"),
            onClick = onAddClicked
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Button")
            Spacer(Modifier.width(8.dp))
            Text(text = "Add")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NamesList(state: LazyListState, names: List<String>, onItemClicked: (String) -> Unit = {}) {
    LazyColumn(
        modifier = Modifier.testTag("names_lazy_column"),
        state = state
    ) {
        items(names.reversed(), key = { it }) {
            NameItem(modifier = Modifier.animateItemPlacement(), name = it) { onItemClicked(it) }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NameItem(modifier: Modifier = Modifier, name: String, onItemClicked: () -> Unit = {}) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onItemClicked()
            },
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = name
        )
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    ComposeKTTheme {
        MainScreen(
            currentName = "",
            names = MainViewModel.initialNames,
            selectedName = null,
            onEvent = {}
        )
    }
}