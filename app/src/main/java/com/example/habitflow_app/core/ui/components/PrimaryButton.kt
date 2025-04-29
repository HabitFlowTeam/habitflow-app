package com.example.habitflow_app.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.habitflow_app.core.ui.theme.AppTypography
import com.example.habitflow_app.core.ui.theme.Black
import com.example.habitflow_app.core.ui.theme.White

/**
 * A reusable primary button component with customizable properties.
 *
 * @param text Button text
 * @param onClick Click handler
 * @param modifier Optional modifier for customizing the layout
 * @param isEnabled Whether the button is enabled
 * @param isLoading Whether to show a loading indicator
 * @param leadingIcon Optional leading icon
 * @param contentPadding Padding values for the button content
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp, horizontal = 24.dp)
) {
    Button(
        onClick = onClick,
        enabled = isEnabled && !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            contentColor = White,
            disabledContainerColor = Black.copy(alpha = 0.5f),
            disabledContentColor = White.copy(alpha = 0.5f)
        ),
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = White,
                strokeWidth = 2.dp,
                modifier = Modifier.height(24.dp)
            )
        } else {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = White
                )
            }
            Text(
                text = text,
                style = AppTypography.labelLarge,
                textAlign = TextAlign.Center,
                color = White
            )
        }
    }
}
