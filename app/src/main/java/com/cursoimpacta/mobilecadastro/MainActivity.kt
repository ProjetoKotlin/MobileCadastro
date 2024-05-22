package com.cursoimpacta.mobilecadastro

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.cursoimpacta.mobilecadastro.ui.theme.MobileCadastroTheme
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.regex.Pattern


class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: Database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileCadastroTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    dbHelper = Database(this)
                    TelaPrincipal(dbHelper)
                }
            }
        }
    }
}


@Composable
fun TelaPrincipal(db: Database) {
    var telaCadastro by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(color = Color(0xFFD1E0F2))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Header(telaCadastro, onMudarTela = { telaCadastro = it })
        if (telaCadastro) {
            TelaCadastro()

        } else {
            TelaBuscarUsuario()
        }
    }
}


@Composable
fun TelaCadastro() {
    CamposUsuario()
}

@Composable
fun TelaBuscarUsuario() {
    CampoBuscarUsuario()
    PerfilEncontrado()
}

//@Preview(showBackground = true)
//@Composable
//fun Teste() {
//    TelaPrincipal()
//}