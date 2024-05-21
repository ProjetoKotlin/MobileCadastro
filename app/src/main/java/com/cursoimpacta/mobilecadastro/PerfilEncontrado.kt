package com.cursoimpacta.mobilecadastro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilEncontrado() {
    Column(
        modifier = Modifier
            .width(280.dp)
            .background(color = Color(0xFFD0E2F1)),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Perfil Encontrado:", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically // Alinhamento vertical ao centro
        ) {
            TextWithImage(
                text = "Alterar",
                imagePainter = painterResource(id = R.drawable.ic_edit)
            )
            Spacer(modifier = Modifier.width(30.dp))
            TextWithImage(
                text = "Excluir",
                imagePainter = painterResource(id = R.drawable.ic_delete)
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Spacer para preencher o espaço vazio
    }
}

@Composable
fun TextWithImage(text: String, imagePainter: Painter) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = imagePainter,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Preview
@Composable
fun AppPreview() {
    PerfilEncontrado()
}
