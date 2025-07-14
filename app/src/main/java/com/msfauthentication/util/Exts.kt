package com.msfauthentication.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.time.Duration.Companion.seconds

fun HttpException.getErrorMessage(): String {
    return try {
        val message = response()?.errorBody()?.string().toString()
        JSONObject(message).getString("message")
    } catch (e: Exception) {
        e.message.toString()
    }
}

@SuppressLint("HardwareIds")
fun Context.getCurrentDeviceId(): String {
    return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}

fun getPhoneModel(): String {
    return "${Build.BRAND} ${Build.MODEL}"
}

private fun createCustomTempFile(context: Context): File {
    val cacheDir = context.cacheDir
    return File.createTempFile(System.currentTimeMillis().toString(), ".jpg", cacheDir)
}

fun Uri.toFile(context: Context): File {
    val contentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(this) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}