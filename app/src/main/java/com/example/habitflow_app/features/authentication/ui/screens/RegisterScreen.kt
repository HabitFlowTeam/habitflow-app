package com.example.habitflow_app.features.authentication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habitflow_app.core.ui.components.InputTextField
import com.example.habitflow_app.core.ui.components.PrimaryButton
import com.example.habitflow_app.core.ui.components.SecondaryButton
import com.example.habitflow_app.core.ui.theme.AppTypography
import com.example.habitflow_app.core.ui.theme.Red500
import com.example.habitflow_app.core.ui.theme.Zinc500
import com.example.habitflow_app.features.authentication.ui.components.Logo
import com.example.habitflow_app.features.authentication.ui.viewmodel.RegisterEvent
import com.example.habitflow_app.features.authentication.ui.viewmodel.RegisterViewModel
import com.example.habitflow_app.navigation.NavDestinations

/**
 * Screen that handles user registration process.
 * Displays a form with user registration fields and handles validation.
 *
 * @param navController Navigation controller for screen transitions
 * @param viewModel ViewModel that handles registration logic and state management
 */
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    // Collect UI state from ViewModel
    val state by viewModel.uiState.collectAsState()

    // Handle successful registration navigation
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate(NavDestinations.MAIN) {
                // Clear back stack including this screen
                popUpTo(NavDestinations.REGISTER) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* App logo */
            Logo()

            Text(
                text = "Crea tu cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Comienza a mejorar tus hábitos hoy",
                fontSize = 20.sp,
                color = Zinc500
            )


            /* Full Name Input Field */
            InputTextField(
                value = state.fullName,
                onValueChange = { viewModel.onEvent(RegisterEvent.FullNameChanged(it)) },
                label = "Nombre completo",
                isError = state.fullNameError != null,
                errorMessage = state.fullNameError,
                imeAction = ImeAction.Next,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Nombre completo",
                        tint = if (state.fullNameError != null) Red500 else Zinc500
                    )
                }
            )

            /* Username Input Field */
            InputTextField(
                value = state.username,
                onValueChange = { viewModel.onEvent(RegisterEvent.UsernameChanged(it)) },
                label = "Nombre de usuario",
                isError = state.usernameError != null,
                errorMessage = state.usernameError,
                imeAction = ImeAction.Next,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Nombre de usuario",
                        tint = if (state.usernameError != null) Red500 else Zinc500
                    )
                }
            )

            /* Email Input Field */
            InputTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(RegisterEvent.EmailChanged(it)) },
                label = "Correo electrónico",
                isError = state.emailError != null,
                errorMessage = state.emailError,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Correo electrónico",
                        tint = if (state.emailError != null) Red500 else Zinc500
                    )
                }
            )

            /* Password Input Field */
            InputTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(RegisterEvent.PasswordChanged(it)) },
                label = "Contraseña",
                isError = state.passwordError != null,
                errorMessage = state.passwordError,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                imeAction = ImeAction.Next,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Contraseña",
                        tint = if (state.passwordError != null) Red500 else Zinc500
                    )
                }
            )

            /* Confirm Password Input Field */
            InputTextField(
                value = state.confirmPassword,
                onValueChange = { viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
                label = "Confirmar contraseña",
                isError = state.confirmPasswordError != null,
                errorMessage = state.confirmPasswordError,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                imeAction = ImeAction.Done,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Confirmar contraseña",
                        tint = if (state.confirmPasswordError != null) Red500 else Zinc500
                    )
                }
            )

            /* Terms and Conditions Checkbox */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = state.termsAccepted,
                    onCheckedChange = { viewModel.onEvent(RegisterEvent.TermsAcceptedChanged(it)) },
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Acepto los términos y condiciones",
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            state.termsError?.let { error ->
                Text(
                    text = error,
                    color = Red500,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            /* Error Message Display */
            state.error?.let { error ->
                Text(
                    text = error,
                    color = Red500,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            /* Register Button */
            PrimaryButton(
                text = "Registrarse",
                onClick = { viewModel.onEvent(RegisterEvent.Submit) },
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            /* Login Navigation Link */
            SecondaryButton(
                text = "¿Ya tienes una cuenta? Inicia sesión",
                onClick = {
                    navController.navigate(NavDestinations.LOGIN) {
                        popUpTo(NavDestinations.REGISTER) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}