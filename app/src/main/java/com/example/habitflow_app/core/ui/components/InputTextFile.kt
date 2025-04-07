package com.example.habitflow_app.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.habitflow_app.core.ui.theme.AppTypography
import com.example.habitflow_app.core.ui.theme.Black
import com.example.habitflow_app.core.ui.theme.Blue500
import com.example.habitflow_app.core.ui.theme.Red500
import com.example.habitflow_app.core.ui.theme.Zinc500
import com.example.habitflow_app.core.ui.theme.Surface

/**
 * A reusable input text field component with built-in validation support.
 *
 * @param value Current text value
 * @param onValueChange Callback when text changes
 * @param label Label for the text field
 * @param modifier Optional modifier for customizing the layout
 * @param placeholder Optional placeholder text
 * @param isError Whether the field has an error
 * @param errorMessage Error message to display
 * @param keyboardType Keyboard type to display
 * @param visualTransformation Visual transformation for the text (e.g., password masking)
 * @param leadingIcon Optional leading icon
 * @param trailingIcon Optional trailing icon with action
 * @param singleLine Whether the field should be a single line
 * @param maxLines Maximum number of lines
 * @param enabled Whether the field is enabled
 * @param readOnly Whether the field is read-only
 */
@Composable
fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onDone: (() -> Unit)? = null,
    onNext: (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label, color = Black, style = AppTypography.bodyMedium) },
            placeholder = if (placeholder.isNotEmpty()) {
                { Text(text = placeholder, color = Zinc500, style = AppTypography.bodyMedium) }
            } else null,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    when (imeAction) {
                        ImeAction.Next -> {
                            focusManager.moveFocus(FocusDirection.Down)
                            onNext?.invoke()
                        }

                        ImeAction.Done -> {
                            focusManager.clearFocus()
                            onDone?.invoke()
                        }

                        else -> {}
                    }
                },
                onDone = {
                    focusManager.clearFocus()
                    onDone?.invoke()
                }
            ),
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            maxLines = maxLines,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
                disabledContainerColor = Surface.copy(alpha = 0.5f),
                errorContainerColor = Surface,
                focusedTextColor = Black,
                unfocusedTextColor = Black,
                disabledTextColor = Zinc500,
                errorTextColor = Red500,
                focusedLabelColor = Blue500,
                unfocusedLabelColor = Zinc500,
                errorLabelColor = Red500,
            ),
            modifier = Modifier.fillMaxWidth(),
            textStyle = AppTypography.bodyMedium.copy(color = Black)
        )

        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = Red500,
                style = AppTypography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
