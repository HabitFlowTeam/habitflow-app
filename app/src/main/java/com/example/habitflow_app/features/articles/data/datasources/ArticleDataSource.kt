package com.example.habitflow_app.features.articles.data.datasources

import android.util.Log
import com.example.habitflow_app.core.exceptions.ApiException
import com.example.habitflow_app.core.exceptions.NetworkConnectionException
import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.models.ProfileArticle
import com.example.habitflow_app.domain.models.RankedArticle
import com.example.habitflow_app.features.articles.data.dto.ArticleLikeRequest
import com.example.habitflow_app.features.articles.data.dto.createDeleteFilter
import com.example.habitflow_app.features.authentication.data.datasources.LocalDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.content.Context
import android.net.Uri
import com.example.habitflow_app.BuildConfig
import com.example.habitflow_app.features.articles.data.dto.CreateArticleRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


/**
 * Data source for fetching article data from the Directus API service.
 * Handles network and API exceptions.
 */
class ArticleDataSource @Inject constructor(
    private val directusApiService: DirectusApiService,
    private val localDataStore: LocalDataStore,
    private val extractInfoToken: ExtractInfoToken
) {
    companion object {
        private const val TAG = "ArticleDataSource"
    }

    /**
     * Retrieves the list of articles from the Directus service.
     *
     * @return List of [ProfileArticle] objects fetched from the backend.
     * @throws ApiException if an API error or unexpected error occurs.
     * @throws NetworkConnectionException if a network connection error occurs.
     */
    suspend fun getUserArticles(userId: String): List<ProfileArticle> =
        withContext(Dispatchers.IO) {
            try {
                val response = directusApiService.getUserArticles(userId)
                if (!response.isSuccessful) {
                    Log.e(
                        TAG,
                        "Error al obtener artículos: ${response.code()} ${response.message()}"
                    )
                }
                val articles = response.body()?.data ?: emptyList()
                articles.map { dto ->
                    ProfileArticle(
                        title = dto.title,
                        imageUrl = dto.image_url,
                        likes = dto.likes_count,
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener artículos", e)
                throw Exception("Error al obtener artículos: ${e.message}")
            }
        }

    suspend fun getRankedArticles(): List<RankedArticle> = withContext(Dispatchers.IO) {
        try {
            val response = directusApiService.getRankedArticles()
            if (!response.isSuccessful) {
                Log.e(TAG, "Error al obtener artículos: ${response.code()} ${response.message()}")
                throw Exception("Error al obtener artículos: ${response.code()}")
            }
            val articles = response.body()?.data ?: emptyList()
            articles.map { dto ->
                RankedArticle(
                    title = dto.title,
                    content = dto.content,
                    authorName = dto.author_name,
                    authorImageUrl = dto.author_image_url,
                    likesCount = dto.likes_count,
                    articleId = dto.id,
                    categoryName = dto.category_name,
                    createdAt = dto.created_at,
                    imageUrl = dto.image_url,
                    userId = dto.user_id
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener artículos", e)
            throw Exception("Error al obtener artículos: ${e.message}")
        }
    }

    /**
     * Gets the current user ID from the token
     * @return The current user ID
     */
    private suspend fun getCurrentUserId(): String {
        val token = localDataStore.getAccessTokenOnce() 
            ?: throw Exception("No hay token de acceso")
        return extractInfoToken.extractUserIdFromToken(token)
    }

    /**
     * Adds a like to an article for the current user
     * @param articleId The ID of the article to like
     * @return Boolean indicating if the operation was successful
     */
    suspend fun likeArticle(articleId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "PASO 6: Iniciando likeArticle en DataSource")
            val userId = getCurrentUserId()
            Log.d(TAG, "PASO 6.1: UserId obtenido del token: $userId")
            
            val request = ArticleLikeRequest(
                user_id = userId,
                article_id = articleId
            )
            Log.d(TAG, "PASO 6.2: Request creado: $request")

            // Convertir el request a JSON para ver exactamente qué se está enviando
            val gson = com.google.gson.GsonBuilder().setPrettyPrinting().create()
            val jsonRequest = gson.toJson(request)
            Log.d(TAG, "PASO 6.3: JSON del request:\n$jsonRequest")
            
            Log.d(TAG, "PASO 7: Enviando request a Directus")
            val response = directusApiService.createArticleLike(request)
            Log.d(TAG, "PASO 8: Respuesta recibida de Directus")
            
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "PASO 8.1: Error en la respuesta")
                Log.e(TAG, "Código HTTP: ${response.code()}")
                Log.e(TAG, "Error body: $errorBody")
                Log.e(TAG, "Request que causó el error: $jsonRequest")
                Log.e(TAG, "Headers de la respuesta: ${response.headers()}")
            } else {
                val responseData = response.body()?.toString()
                Log.d(TAG, "PASO 8.2: Respuesta exitosa")
                Log.d(TAG, "Datos de la respuesta: $responseData")
            }
            
            Log.d(TAG, "PASO 9: Finalizando likeArticle en DataSource - Success: ${response.isSuccessful}")
            response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "PASO X: Error en likeArticle", e)
            Log.e(TAG, "Detalles del error: ${e.message}")
            Log.e(TAG, "Stack trace: ${e.stackTraceToString()}")
            false
        }
    }

    /**
     * Removes a like from an article for the current user
     * @param articleId The ID of the article to unlike
     * @return Boolean indicating if the operation was successful
     */
    suspend fun unlikeArticle(articleId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val userId = getCurrentUserId()
            Log.d(TAG, "Attempting to unlike article. ArticleId: $articleId, UserId: $userId")
            
            // Primero obtenemos el ID del like
            val likeResponse = directusApiService.getLikeId(articleId, userId)
            if (!likeResponse.isSuccessful) {
                Log.e(TAG, "Error getting like ID. Code: ${likeResponse.code()}")
                return@withContext false
            }

            val likes = likeResponse.body()?.data
            if (likes.isNullOrEmpty()) {
                Log.e(TAG, "No like record found")
                return@withContext false
            }

            val likeId = likes[0]["id"]
            if (likeId == null) {
                Log.e(TAG, "Like record found but no ID")
                return@withContext false
            }

            Log.d(TAG, "Found like record with ID: $likeId")

            // Ahora eliminamos el registro usando su ID
            val response = directusApiService.deleteArticleLike(likeId)
            Log.d(TAG, "Unlike response received. Success: ${response.isSuccessful}, Code: ${response.code()}")
            
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Error unliking article. Code: ${response.code()}, Error: $errorBody")
            }
            
            response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "Exception while unliking article", e)
            false
        }
    }

    /**
     * Checks if the current user has liked a specific article
     * @param articleId The ID of the article to check
     * @return Boolean indicating if the user has liked the article
     */
    suspend fun isArticleLiked(articleId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val userId = getCurrentUserId()
            Log.d(TAG, "Checking if article is liked. ArticleId: $articleId, UserId: $userId")
            
            val response = directusApiService.isArticleLiked(articleId, userId)
            Log.d(TAG, "IsLiked response received. Success: ${response.isSuccessful}, Code: ${response.code()}")
            
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Error checking if article is liked. Code: ${response.code()}, Error: $errorBody")
                return@withContext false
            }
            
            val result = response.body()?.data?.isNotEmpty() ?: false
            Log.d(TAG, "Article liked status: $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Exception while checking if article is liked", e)
            false
        }
    }

    /**
     * Uploads an image file to Directus
     * @param imageUri The URI of the image to upload
     * @param context Android context for reading the file
     * @return The URL of the uploaded image
     */
    suspend fun uploadImage(imageUri: Uri, context: Context): String = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Uploading image: $imageUri")

            val inputStream = context.contentResolver.openInputStream(imageUri)
                ?: throw Exception("Cannot open image file")

            val fileName = "article_image_${System.currentTimeMillis()}.jpg"
            val requestFile = inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", fileName, requestFile)

            val response = directusApiService.uploadFile(body)

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Error uploading image: ${response.code()} - $errorBody")
                throw Exception("Error uploading image: ${response.code()}")
            }

            val fileData = response.body()?.data
                ?: throw Exception("No file data received")

            val imageUrl = "${BuildConfig.DIRECTUS_URL}assets/${fileData.id}"
            Log.d(TAG, "Image uploaded successfully: $imageUrl")

            if (!imageUrl.startsWith("http")) {
                Log.w(TAG, "URL might be malformed: $imageUrl")
            }

            imageUrl
        } catch (e: Exception) {
            Log.e(TAG, "Exception while uploading image", e)
            throw Exception("Error uploading image: ${e.message}")
        }
    }

    /**
     * Creates a new article for the current user
     * @param title The title of the article
     * @param content The content of the article
     * @param imageUri Optional URI of the image to upload
     * @param categoryId The ID of the category the article belongs to
     * @param context Android context for image processing
     * @return The ID of the created article
     */
    suspend fun createArticle(
        title: String,
        content: String,
        imageUri: Uri?,
        categoryId: String,
        context: Context
    ): String = withContext(Dispatchers.IO) {
        try {
            val userId = getCurrentUserId()
            Log.d(TAG, "Creating article for user: $userId")

            // Upload image if provided
            val imageUrl = imageUri?.let { uri ->
                uploadImage(uri, context)
            }

            val request = CreateArticleRequest(
                title = title,
                content = content,
                image_url = imageUrl,
                category_id = categoryId,
                user_id = userId
            )

            val response = directusApiService.createArticle(request)

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Error creating article: ${response.code()} - $errorBody")
                throw Exception("Error creating article: ${response.code()}")
            }

            val articleData = response.body()?.data
                ?: throw Exception("No data received from server")

            Log.d(TAG, "Article created successfully with ID: ${articleData.id}")
            articleData.id
        } catch (e: Exception) {
            Log.e(TAG, "Exception while creating article", e)
            throw Exception("Error creating article: ${e.message}")
        }
    }
}
