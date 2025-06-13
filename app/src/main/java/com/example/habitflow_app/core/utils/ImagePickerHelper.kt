package com.example.habitflow_app.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream

object ImagePickerHelper {

    /**
     * Converts an image URI to a Base64 string for uploading
     * @param context Android context
     * @param uri The image URI from the picker
     * @param maxWidth Maximum width for compression (default 800px)
     * @param quality JPEG compression quality (0-100, default 80)
     * @return Base64 encoded string of the compressed image
     */
    suspend fun uriToBase64(
        context: Context,
        uri: Uri,
        maxWidth: Int = 800,
        quality: Int = 80
    ): String? = withContext(Dispatchers.IO) {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap == null) return@withContext null

            // Compress and resize if needed
            val resizedBitmap = if (bitmap.width > maxWidth) {
                val ratio = maxWidth.toFloat() / bitmap.width
                val newHeight = (bitmap.height * ratio).toInt()
                Bitmap.createScaledBitmap(bitmap, maxWidth, newHeight, true)
            } else {
                bitmap
            }

            // Convert to Base64
            val byteArrayOutputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()

            "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Gets the MIME type of the selected image
     */
    fun getMimeType(context: Context, uri: Uri): String? {
        return context.contentResolver.getType(uri)
    }
}