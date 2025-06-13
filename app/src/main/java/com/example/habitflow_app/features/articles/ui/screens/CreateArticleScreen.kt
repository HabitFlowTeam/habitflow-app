package com.example.habitflow_app.features.articles.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.habitflow_app.core.ui.components.InputTextField
import com.example.habitflow_app.core.ui.components.PrimaryButton
import com.example.habitflow_app.core.ui.components.SecondaryButton
import com.example.habitflow_app.core.ui.theme.*
import com.example.habitflow_app.features.articles.ui.viewmodel.CreateArticleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateArticleScreen(
    navController: NavController,
    viewModel: CreateArticleViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val title by viewModel.title.collectAsState()
    val content by viewModel.content.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    var expandedDropdown by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateSelectedImageUri(uri)
    }

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            navController.popBackStack()
        }
    }

    error?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Black
                )
            }

            Text(
                text = "Nuevo Artículo",
                style = AppTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Black,
                modifier = Modifier.weight(1f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Información del artículo",
                style = AppTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            InputTextField(
                value = title,
                onValueChange = viewModel::updateTitle,
                label = "Título",
                placeholder = "Escribe un título atractivo...",
                imeAction = ImeAction.Next,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Text(
                    text = "Categoría",
                    style = AppTypography.bodyMedium,
                    color = Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name ?: "Selecciona una categoría",
                        onValueChange = { },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Dropdown"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Surface,
                            unfocusedContainerColor = Surface,
                            focusedTextColor = Black,
                            unfocusedTextColor = if (selectedCategory != null) Black else Zinc500
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    viewModel.updateSelectedCategory(category)
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            Text(
                text = "Imagen del artículo",
                style = AppTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 2.dp,
                        color = Zinc200,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(Zinc50)
                    .clickable {
                        imagePickerLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Añadir imagen",
                            tint = Zinc400,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Añadir",
                            style = AppTypography.bodyMedium,
                            color = Zinc400
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Contenido",
                style = AppTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            InputTextField(
                value = content,
                onValueChange = viewModel::updateContent,
                label = "Contenido",
                placeholder = "Escribe el contenido de tu artículo aquí...",
                singleLine = false,
                maxLines = 10,
                imeAction = ImeAction.Done,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .height(200.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    text = "Cancelar",
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                )

                PrimaryButton(
                    text = "Publicar",
                    onClick = { viewModel.createArticle(context) },
                    isLoading = isLoading,
                    isEnabled = viewModel.isFormValid(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}