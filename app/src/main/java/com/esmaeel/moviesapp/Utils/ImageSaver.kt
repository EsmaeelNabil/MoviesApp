package com.esmaeel.moviesapp.Utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object ImageSaver {
    fun saveToGallery(
        context: Context,
        bitmap: Bitmap,
        albumName: String,
        onSaved: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        doSafely({
            val filename = "${System.currentTimeMillis()}.png"
            val write: (OutputStream) -> Boolean = {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        "${Environment.DIRECTORY_DCIM}/$albumName"
                    )
                }

                context.contentResolver.let {
                    it.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                        ?.let { uri ->
                            it.openOutputStream(uri)?.let(write)
                        }
                }
            } else {
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        .toString() + File.separator + albumName
                val file = File(imagesDir)
                if (!file.exists()) {
                    file.mkdir()
                }
                val image = File(imagesDir, filename)
                write(FileOutputStream(image))
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(image.absolutePath),
                    arrayOf("image/jpeg"),
                    null
                );

            }
            onSaved.invoke()
        }, {
            onError(it ?: "Unknown Error!")
        })
    }
}