package ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.viewModel

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import ge.anthony_kharin.tapyoutesttask.di.qualifiers.ApplicationCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

private const val FILE_PREFIX = "TestTapYouKharinAnton"
private const val TIME_PATTERN = "yyyyMMdd_HHmmss"
private const val IMAGE_FORMAT = "png"
private const val MIME_TYPE = "image/$IMAGE_FORMAT"
private const val DIRECTORY_NAME = "TestTapYouKharinAntonCharts"

class ChartSaver @Inject constructor(
    private val context: Context,
    @ApplicationCoroutineScope private val applicationScope: CoroutineScope,
) {
    fun saveChart(bitmap: Bitmap) {
        applicationScope.launch(Dispatchers.IO) {
            val timeStamp = SimpleDateFormat(TIME_PATTERN, Locale.getDefault()).format(Date())
            val displayName = "$FILE_PREFIX$timeStamp.$IMAGE_FORMAT"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveToMediaStoreQ(bitmap, displayName)
            } else {
                saveToExternalStorageLegacy(bitmap, displayName)
            }
        }
    }

    private fun saveToMediaStoreQ(bitmap: Bitmap, displayName: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE)
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "${Environment.DIRECTORY_DCIM}/$DIRECTORY_NAME"
            )
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: return

        try {
            resolver.openOutputStream(uri).use { outputStream ->
                if (outputStream == null) {
                    resolver.delete(uri, null, null)
                    return
                }
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        } catch (e: Exception) {
            resolver.delete(uri, null, null)
            e.printStackTrace()
        }
    }

    @Suppress("DEPRECATION")
    private fun saveToExternalStorageLegacy(bitmap: Bitmap, fileName: String) {
        val picturesDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM
            ), DIRECTORY_NAME
        )
        if (!picturesDir.exists() && !picturesDir.mkdirs()) {
            return
        }

        val imageFile = File(picturesDir, fileName)
        try {
            FileOutputStream(imageFile).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
            }
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
                data = Uri.fromFile(imageFile)
            }
            context.sendBroadcast(mediaScanIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}