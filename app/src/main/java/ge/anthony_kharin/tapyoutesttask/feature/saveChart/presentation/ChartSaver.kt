package ge.anthony_kharin.tapyoutesttask.feature.saveChart.presentation

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.StringRes
import ge.anthony_kharin.tapyoutesttask.R
import ge.anthony_kharin.tapyoutesttask.di.qualifiers.ApplicationCoroutineScope
import ge.anthony_kharin.tapyoutesttask.feature.saveChart.domain.SavingChartException
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
private const val COMPRESS_QUALITY = 100

// В этом месте стоит раскрыть зачем нужен applicationScope
// Т.к. сохранение файла не самая быстрая операция, то есть высокая вероятность,
// что мы начнем сохранять файл и уйдем с экрана,
// а если использовать viewModelScope, то джоба закенселится при уничтожении ViewModel
class ChartSaver @Inject constructor(
    private val context: Context,
    private val resources: Resources,
    @ApplicationCoroutineScope private val applicationScope: CoroutineScope,
) {
    fun saveChart(bitmap: Bitmap) {
        applicationScope.launch(Dispatchers.IO) {
            showNotifyToast(R.string.chart_saving_notification)
            val timeStamp = SimpleDateFormat(TIME_PATTERN, Locale.getDefault()).format(Date())
            val displayName = "${FILE_PREFIX}$timeStamp.${IMAGE_FORMAT}"

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveToMediaStoreQ(bitmap, displayName)
                } else {
                    saveToExternalStorageLegacy(bitmap, displayName)
                }
                showNotifyToast(R.string.chart_saved_notification)
            } catch (e: SavingChartException) {
                e.printStackTrace()
                showNotifyToast(R.string.chart_saving_error_notification)
            }
        }
    }

    private fun saveToMediaStoreQ(bitmap: Bitmap, displayName: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE)
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "${Environment.DIRECTORY_DCIM}/${DIRECTORY_NAME}"
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
                bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, outputStream)
            }
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        } catch (e: Exception) {
            resolver.delete(uri, null, null)
            throw SavingChartException(e.message)
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
                bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, fos)
                fos.flush()
            }
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
                data = Uri.fromFile(imageFile)
            }
            context.sendBroadcast(mediaScanIntent)
        } catch (e: Exception) {
            throw SavingChartException(e.message)
        }
    }

    // Это некий "костыль", поскольку в ТЗ не было сказано ни о нотификейшенах, ни о сервисах
    // На мой взгляд нужна нотификация, чтобы показать, что что-то происходит,
    // в качестве упрощения был использован такой тост
    private fun showNotifyToast(@StringRes stringId: Int) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                context,
                resources.getString(stringId),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}