package com.cursoimpacta.mobilecadastro

import android.graphics.Paint.Align
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cursoimpacta.mobilecadastro.ui.theme.MobileCadastroTheme
import java.time.format.TextStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileCadastroTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Header()
                }
            }
        }
    }
}

@Composable
fun Header(modifier: Modifier = Modifier) {
    var btn_clicked by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color(0xFFFFFFFF))
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.height(50.dp),
                onClick = { btn_clicked = !btn_clicked },
                colors = if (btn_clicked) ButtonDefaults.buttonColors(
                    containerColor = Color(
                        0xFF809CBE
                    )
                ) else ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = "Cadastrar",
                    fontSize = 18.sp,
                    color = if (btn_clicked) Color(0xFFFFFFFF) else Color(0xFF000000)
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Button(
                modifier = Modifier.height(50.dp),
                onClick = { btn_clicked = !btn_clicked },
                colors = if (btn_clicked) ButtonDefaults.buttonColors(
                    containerColor = Color
                        .Transparent
            ) else ButtonDefaults.buttonColors(containerColor = Color(0xFF809CBE)),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = "Buscar Usuário",
                    fontSize = 18.sp,
                    color = if (btn_clicked) Color(0xFF000000) else Color(0xFFFFFFFF)
                )
            }
        }
    }
}

@Preview
@Composable
fun GreetingPreview() {
    MobileCadastroTheme {
        Header()
    }
}

