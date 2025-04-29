package com.example.habitflow_app.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.habitflow_app.core.ui.theme.AppTypography
import com.example.habitflow_app.core.ui.theme.Black

/**
 * A reusable secondary button component with customizable properties.
 *
 * @param text Button text
 * @param onClick Click handler
 * @param modifier Optional modifier for customizing the layout
 * @param isEnabled Whether the button is enabled
 * @param leadingIcon Optional leading icon
 * @param contentPadding Padding values for the button content
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp, horizontal = 24.dp)
) {
    OutlinedButton(
        onClick = onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isEnabled) Black else Black.copy(alpha = 0.5f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = Black,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Black.copy(alpha = 0.5f)
        ),
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = if (isEnabled) Black else Black.copy(alpha = 0.5f)
            )
        }
        Text(
            text = text,
            style = AppTypography.labelLarge,
            textAlign = TextAlign.Center,
            color = if (isEnabled) Black else Black.copy(alpha = 0.5f)
        )
    }
}
