package com.example.habitflow_app.features.authentication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.example.habitflow_app.features.authentication.ui.viewmodel.LoginEvent
import com.example.habitflow_app.features.authentication.ui.viewmodel.LoginViewModel
import com.example.habitflow_app.navigation.NavDestinations

/**
 * Screen that handles user login process.
 * Displays a form with user login fields and handles validation.
 *
 * @param navController Navigation controller for screen transitions
 * @param viewModel ViewModel that handles login logic and state management
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    // Collect UI state from ViewModel
    val state by viewModel.uiState.collectAsState()

    // Handle successful login navigation
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate(NavDestinations.MAIN) {
                // Clear back stack including this screen
                popUpTo(NavDestinations.LOGIN) { inclusive = true }
            }
        }
    }

    // Main screen scaffold
    Scaffold{ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .padding(vertical = 128.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /* App logo */
            Logo()

            Text(
                text = "¡Bienvenido!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Inicia sesión para continuar",
                fontSize = 20.sp,
                color = Zinc500
            )

            Spacer(modifier = Modifier.padding(8.dp))

            /* Email Input Field */
            InputTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
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
                onValueChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
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
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    style = AppTypography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { navController.navigate(NavDestinations.FORGOT_PASSWORD) }
                )
            }

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
                text = "Iniciar sesión",
                onClick = { viewModel.onEvent(LoginEvent.Submit) },
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            /* Login Navigation Link */
            SecondaryButton(
                text = "¿No tienes una cuenta? Registrate",
                onClick = {
                    navController.navigate(NavDestinations.REGISTER) {
                        popUpTo(NavDestinations.LOGIN) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            /* Error Message Display */
            state.error?.let { error ->
                Text(
                    text = error,
                    color = Red500,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}