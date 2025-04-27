package com.example.habitflow_app.features.authentication.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun Logo() {
    AsyncImage(
        //este es el link de mi cloudinary(Pablo), pero ya cuando configuren eso y cambien el cloudinary ps cambian este link
        model = "https://res.cloudinary.com/duquztnck/image/upload/v1745707390/Logo_dbfxty.png",
        contentDescription = "logo",
        modifier = Modifier.height(100.dp).width(240.dp)
    )
}