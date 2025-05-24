package com.example.habitflow_app.features.habits.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitflow_app.domain.models.Category
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun FrequencySelector(
    isDailySelected: Boolean,
    onFrequencySelected: (Boolean) -> Unit
) {
    Column {
        Text(
            text = "Frecuencia",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onFrequencySelected(true) }
        ) {
            RadioButton(
                selected = isDailySelected,
                onClick = { onFrequencySelected(true) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF000000)
                )
            )
            Text(
                text = "Diario",
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onFrequencySelected(false) }
        ) {
            RadioButton(
                selected = !isDailySelected,
                onClick = { onFrequencySelected(false) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF000000)
                )
            )
            Text(
                text = "Días específicos",
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun DaysSelector(
    selectedDays: List<String>,
    onDaySelected: (String, Boolean) -> Unit
) {
    val days = listOf(
        "L" to "d3b15c58-711f-40d9-9f4b-06d0d6e925d1", // Lunes
        "M" to "b9b3995e-c6a5-46c7-bf8a-f1c1c2e65dd6", // Martes
        "M" to "4afe91c2-9851-4af7-b282-39a543989ea3", // Miércoles
        "J" to "ea5e7c7a-182c-4b49-8b2d-2162cd138384", // Jueves
        "V" to "22f2bf21-fcbd-473f-98d2-96ba47fabe16", // Viernes
        "S" to "f31a5698-2a4d-4818-8a0b-e7f843b9ec14", // Sábado
        "D" to "82a4b1c9-72a8-4e91-aaa4-2c92d30b810f"  // Domingo
    )

    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text(
            text = "Selecciona los días",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            days.forEach { (dayLabel, dayId) ->
                val isSelected = selectedDays.contains(dayId)
                DayCircle(
                    day = dayLabel,
                    isSelected = isSelected,
                    onClick = { onDaySelected(dayId, !isSelected) }
                )
            }
        }
    }
}

@Composable
fun DayCircle(
    day: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) Color(0xFF00C853) else Color.Transparent
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF00C853) else Color.LightGray,
                shape = CircleShape
            )
            .clickable(onClick = onClick)
    ) {
        Text(
            text = day,
            fontSize = 14.sp,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

@Composable
fun ReminderSection(
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    reminderTime: LocalTime?,
    onTimeChange: (LocalTime) -> Unit
) {
    Column {
        Text(
            text = "Recordatorios",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Activar Recordatorios",
                fontSize = 15.sp
            )

            Switch(
                checked = isEnabled,
                onCheckedChange = onEnabledChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF000000)
                )
            )
        }

        if (isEnabled) {
            Spacer(modifier = Modifier.height(8.dp))
            TimeSelector(
                selectedTime = reminderTime ?: LocalTime.of(8, 0),
                onTimeSelected = onTimeChange
            )
        }
    }
}

@Composable
fun TimeSelector(
    selectedTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = "Hora del Recordatorio",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                .clickable { showDialog = true }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = selectedTime.format(timeFormatter),
                fontSize = 14.sp
            )

        }
    }

    if (showDialog) {
        BasicTimePickerDialog(
            initialTime = selectedTime,
            onTimeSelected = {
                onTimeSelected(it)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun BasicTimePickerDialog(
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    var hour by remember { mutableStateOf(initialTime.hour) }
    var minute by remember { mutableStateOf(initialTime.minute) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar hora") },
        text = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                NumberPicker(
                    value = hour,
                    onValueChange = { hour = it },
                    range = 0..23
                )

                Text(
                    text = ":",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                NumberPicker(
                    value = minute,
                    onValueChange = { minute = it },
                    range = 0..59
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onTimeSelected(LocalTime.of(hour, minute)) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                if (value < range.last) onValueChange(value + 1)
                else onValueChange(range.first)
            }
        ) {
            Text("▲", fontSize = 18.sp)
        }

        Text(
            text = value.toString().padStart(2, '0'),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            onClick = {
                if (value > range.first) onValueChange(value - 1)
                else onValueChange(range.last)
            }
        ) {
            Text("▼", fontSize = 18.sp)
        }
    }
}

@Composable
fun HabitNameField(
    name: String,
    onNameChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Información básica",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Nombre del hábito",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            placeholder = { Text("Meditación, ejercicio, etc.") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF000000),
                unfocusedBorderColor = Color.LightGray
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun HabitActionButtons(
    onSave: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (onDelete != null) {
            Button(
                onClick = onDelete,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Text("Eliminar hábito")
            }
        }

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text("Guardar hábito")
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar hábito") },
        text = { Text("¿Estás seguro de que quieres eliminar este hábito?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(
    categories: List<Category>,
    selectedCategoryId: String?,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredCategories = remember(searchQuery, categories) {
        if (searchQuery.isBlank()) {
            categories
        } else {
            categories.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    val selectedCategory = remember(selectedCategoryId, categories) {
        categories.find { it.id == selectedCategoryId }
    }

    Column(modifier = modifier) {
        Text(
            text = "Categoría",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCategory?.name ?: "Seleccionar categoría",
                onValueChange = { searchQuery = it },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar categoría...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                filteredCategories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onCategorySelected(category.id)
                            expanded = false
                            searchQuery = ""
                        }
                    )
                }
            }
        }
    }
}