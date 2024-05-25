package com.cursoimpacta.mobilecadastro

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.IOException

@Composable
fun Camera(onBitmapValor: (Bitmap?) -> Unit) {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        try {
            imageBitmap = bitmap
            onBitmapValor(bitmap)
        } catch (e: Exception) {
            Log.e("Camera", "Error capturing image", e)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        try {
            uri?.let {
                imageBitmap = getBitmapFromUri(contentResolver, it)
                onBitmapValor(imageBitmap)
            }
        } catch (e: Exception) {
            Log.e("Camera", "Error selecting image from gallery", e)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { cameraLauncher.launch(null) }) {
            Text("Capture Image")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text("Select from Gallery")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (imageBitmap != null) {
            imageBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured image",
                    modifier = Modifier
                        .size(300.dp),
//                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}


@Throws(IOException::class)
fun getBitmapFromUri(contentResolver: ContentResolver, uri: Uri?): Bitmap? {
    return try {
        contentResolver.openInputStream(uri!!).use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: Exception) {
        Log.e("Camera", "Error getting bitmap from URI", e)
        null
    }
}
