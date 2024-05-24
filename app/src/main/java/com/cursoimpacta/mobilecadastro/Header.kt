package com.cursoimpacta.mobilecadastro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun Header(
    telaCadastro: Boolean,
    onMudarTela: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFFFFFFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.height(50.dp),
                onClick = { onMudarTela(true) },
                colors = if (telaCadastro) ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF809CBE)
                ) else ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = "Cadastrar",
                    fontSize = 18.sp,
                    color = if (telaCadastro) Color(0xFFFFFFFF) else Color(0xFF000000)
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Button(
                modifier = Modifier.height(50.dp),
                onClick = { onMudarTela(false) },
                colors = if (!telaCadastro) ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF809CBE)
                ) else ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = "Buscar Usuário",
                    fontSize = 18.sp,
                    color = if (!telaCadastro) Color(0xFFFFFFFF) else Color(0xFF000000)
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    Header()
//}