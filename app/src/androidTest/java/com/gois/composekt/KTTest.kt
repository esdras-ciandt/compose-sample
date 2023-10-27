package com.gois.composekt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KTTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addNameSection_addButtonClicked_addsNewName() {
        var currentValue by mutableStateOf("")
        var addedName = ""

        composeTestRule.setContent {
            AddNameSection(
                currentValue = currentValue,
                onNameChanged = { currentValue = it },
                onAddClicked = {
                    addedName = currentValue
                    currentValue = ""
                }
            )
        }

        val nameTextField = composeTestRule.onNodeWithTag("name_text_field")
        nameTextField.performTextInput("John Doe")

        val addButton = composeTestRule.onNodeWithTag("add_button")
        addButton.performClick()

        addedName shouldBe "John Doe"
    }
}