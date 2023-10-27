package com.gois.composekt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gois.composekt.ui.theme.ComposeKTTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeBottomSheet(selectedName: String?, onDismiss: () -> Unit) {
    val scope = rememberCoroutineScope()
    val isOpen = selectedName != null
    val sheetState = rememberModalBottomSheetState()

    if (isOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss
        ) {
            BottomSheetContent(selectedName) {
                scope.launch {
                    sheetState.hide()
                    onDismiss()
                }
            }
        }
    }
}

@Composable
fun BottomSheetContent(selectedName: String?, onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append("Hey ")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(selectedName ?: "Unknown")
                }
                append(", welcome to our first compose app!")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(200.dp)
        ) {
            Text(text = "Close")
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview
@Composable
fun BottomSheetContentPreview() {
    ComposeKTTheme {
        BottomSheetContent(selectedName = "Apolo") {}
    }
}