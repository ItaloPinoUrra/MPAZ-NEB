package com.example.mpaz_neb.data.remote

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream

object ArchivoUploader {

    suspend fun subir(context: Context, actividadId: Long, uri: Uri): Boolean {
        return try {
            val resolver = context.contentResolver
            val nombre = obtenerNombre(context, uri) ?: "archivo"
            val mime = resolver.getType(uri) ?: "application/octet-stream"
            val bytes = leerBytes(resolver.openInputStream(uri))

            val requestBody = bytes.toRequestBody(mime.toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("archivo", nombre, requestBody)

            MpazNebInstance.api.subirArchivo(actividadId, part)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun leerBytes(input: InputStream?): ByteArray {
        input ?: return ByteArray(0)
        return input.use { it.readBytes() }
    }

    private fun obtenerNombre(context: Context, uri: Uri): String? {
        val cursor = context.contentResolver.query(uri, null, null, null, null) ?: return null
        cursor.use {
            if (it.moveToFirst()) {
                val idx = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (idx >= 0) return it.getString(idx)
            }
        }
        return null
    }
}