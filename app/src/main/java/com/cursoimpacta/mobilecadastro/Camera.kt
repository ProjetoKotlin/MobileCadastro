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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.IOException

@Composable
fun Camera(onBitmapValor: (Bitmap?) -> Unit) {
    // Nessa função estamos incluindo a possibilidade do usuário capturar uma imagem com a câmera
    // ou selecionar uma imagem da galeria
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

    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { cameraLauncher.launch(null) },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF809CBE))) {
            Text("Capture Image")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { galleryLauncher.launch("image/*") },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF809CBE))) {
            Text("Select from Gallery")
        }
        Spacer(modifier = Modifier.height(16.dp))


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
