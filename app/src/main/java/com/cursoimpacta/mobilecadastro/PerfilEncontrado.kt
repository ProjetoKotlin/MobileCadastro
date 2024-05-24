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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilEncontrado(
    btnAlterarClicked: Boolean,
    onAlterarClicked: (Boolean) -> Unit,
    btnDeletarClicked: Boolean,
    onDeletarClicked: (Boolean) -> Unit
) {

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
            Button(
                onClick = {
                    onAlterarClicked(true)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                TextWithImage(
                    text = "Alterar",
                    imagePainter = painterResource(id = R.drawable.ic_edit)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { onDeletarClicked(true) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                TextWithImage(
                    text = "Excluir",
                    imagePainter = painterResource(id = R.drawable.ic_delete)
                )
            }

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
        Text(text = text, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun Teste() {
    var btnAlterarClicked by remember {
        mutableStateOf(false)
    }
    var btnDeletarClicked by remember {
        mutableStateOf(false)
    }
    PerfilEncontrado(
        btnAlterarClicked,
        { btnAlterarClicked = it },
        btnDeletarClicked,
        { btnDeletarClicked = it }
    )
}
