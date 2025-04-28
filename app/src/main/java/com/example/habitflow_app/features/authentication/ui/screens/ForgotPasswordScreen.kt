package com.example.habitflow_app.features.authentication.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import com.example.habitflow_app.features.authentication.ui.viewmodel.ForgotPasswordEvent
import com.example.habitflow_app.features.authentication.ui.viewmodel.ForgotPasswordViewModel
import com.example.habitflow_app.navigation.NavDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate(NavDestinations.LOGIN) {
                popUpTo(NavDestinations.LOGIN) { inclusive = true }
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .padding(vertical = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Logo()

            Text(
                text = "Recuperar contraseña",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Ingresa tu correo electrónico para recibir instrucciones",
                fontSize = 20.sp,
                color = Zinc500
            )

            Spacer(modifier = Modifier.height(24.dp))

            state.message?.let { message ->
                Text(
                    text = message,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
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

            InputTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(ForgotPasswordEvent.EmailChanged(it)) },
                label = "Correo electrónico",
                isError = state.emailError != null,
                errorMessage = state.emailError,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Correo electrónico",
                        tint = if (state.emailError != null) Red500 else Zinc500
                    )
                }
            )

            PrimaryButton(
                text = "Enviar instrucciones",
                onClick = { viewModel.onEvent(ForgotPasswordEvent.Submit) },
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            SecondaryButton(
                text = "Volver al inicio de sesión",
                onClick = { navController.navigate(NavDestinations.LOGIN) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}