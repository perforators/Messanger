package com.krivochkov.homework_2.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import android.provider.OpenableColumns

fun createFileAndGetPathWithFileNameFromCache(context: Context, uri: Uri): Pair<String, String> {
    var inputStream: FileInputStream? = null
    var outputStream: FileOutputStream? = null
    try {
        val fileName = context.contentResolver.getFileName(uri)
        val parcelFileDescriptor =
            context.contentResolver.openFileDescriptor(uri, "r", null)
        inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
        val file = File(context.cacheDir, fileName)
        outputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
            outputStream.write(buffer, 0, len)
        }
        return file.absolutePath to fileName
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        inputStream?.close()
        outputStream?.close()
    }
    return "" to ""
}

@SuppressLint("Range")
fun ContentResolver.getFileName(uri: Uri): String {
    var result: String? = null
    if (uri.scheme == "content") {
        val fileCursor: Cursor? = query(uri, null, null, null, null)
        fileCursor.use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result!!.substring(cut + 1)
        }
    }
    return result!!
}