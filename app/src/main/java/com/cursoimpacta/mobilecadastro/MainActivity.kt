package com.cursoimpacta.mobilecadastro

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.cursoimpacta.mobilecadastro.ui.theme.MobileCadastroTheme
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.regex.Pattern

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
                    App()
                }
            }
        }
    }
}

fun validaNome(nome: String): String {
    val lista = nome.split(" ")
    if (lista.size >= 2) {
        for (palavra in lista) {
            if (palavra.length < 3) {
                return "Cada nome deve conter no mínimo 3 letras cada!"
            }
        }
        return "Certo"
    } else {
        return "O nome precisa de no mínimo 2 palavras!"
    }
}

fun validaEmail(email: String): String{
    val regex = Regex("[A-Z|a-z|0-9|\\.|_|-]+@[\\w-]+\\.[a-z]{2,}")
    if (regex.matches(email))
        return "Email válido"
    else{
        return "Email inválido"
    }
}

fun validaTelefone(telefone: String): String{
    if (telefone.length == 11){
        return "Telefone válido"
    }
    else {
        return "O telefone deve conter 11 dígitos"
    }
}

@Composable
fun App() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFD1E0F2)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CampoUsuario(false)
    }

}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CampoUsuario(registrado: Boolean) {
    val api = remember { ViaCepService.create() }
    val coroutineScope = rememberCoroutineScope()
    val dados by remember {
        mutableStateOf(DadosPessoais("", "", "", "9", "", ""))
    }

    var infoCep by remember {
        mutableStateOf<Endereco?>(null)
    }
    val colorWhite = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.White,
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp),
        modifier = Modifier
            .width(280.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {

        TextField(
            dados.nome, { dados.nome = it }, Modifier.clip(RoundedCornerShape(8.dp)),
            label = { Text(text = "Nome") },

            colors = colorWhite
        )

        Text(text = validaNome(dados.nome))

        TextField(
            value = dados.email,
            onValueChange = { dados.email = it },
            label = { Text(text = "Email") },

            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
            colors = colorWhite
        )
        Text(text = validaEmail(dados.email))

        Text(
            text = "Endereço:",
            modifier = Modifier
                .align(Alignment.Start)
        )
        TextField(value = dados.cep, onValueChange = {
            if (it.isDigitsOnly()) {
                dados.cep = it
            }
        }, label = { Text(text = "CEP") },
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp)),
            colors = colorWhite,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        var msgStatus = ""
        if (dados.cep.length == 8) {
            coroutineScope.launch {
                msgStatus = "Loading"
                lateinit var response: Response<Endereco>
                response = api.procuraCep(dados.cep)
                if (response.isSuccessful && !response.body()!!.erro) {
                    infoCep = response.body()
                } else {
                    msgStatus = "CEP inválido!"
                }
            }
            if (infoCep == null) {
                Text(text = msgStatus)
            }
        }

        TextField(
            value = infoCep?.logradouro ?: "",
            onValueChange = { infoCep?.logradouro = it },
            label = {
                Text(
                    text = "Logradouro"
                )
            },
            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
            colors = colorWhite
        )

        TextField(
            value = infoCep?.bairro ?: "",
            onValueChange = { infoCep?.bairro = it },
            label = {
                Text(
                    text = "Bairro"
                )
            },
            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
            colors = colorWhite
        )
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            TextField(
                value = dados.numero,
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        dados.numero = it
                    }
                },
                label = {
                    Text(
                        text = "Número"
                    )
                },
                modifier = Modifier
                    .width(110.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(
                value = dados.complemento,
                onValueChange = {dados.complemento = it},
                label = {
                    Text(
                        text = "Complemento"
                    )
                },
                modifier = Modifier
                    .width(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite,

                )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            TextField(
                value = infoCep?.localidade ?: "",
                onValueChange = { infoCep?.localidade = it },
                label = {
                    Text(
                        text = "Cidade"
                    )
                },
                modifier = Modifier
                    .width(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite
            )
            TextField(
                value = infoCep?.uf ?: "",
                onValueChange = { infoCep?.uf = it },
                label = {
                    Text(
                        text = "Estado"
                    )
                },
                modifier = Modifier
                    .width(110.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite
            )
        }
        Text(text = "Contato:", modifier = Modifier.align(Alignment.Start))
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            TextField(
                value = infoCep?.ddd ?: "",
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        infoCep?.ddd = it
                    }
                },
                label = { Text(text = "DDD") },
                modifier = Modifier
                    .width(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(
                value = dados.telefone,
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        dados.telefone = it
                    }
                },
                label = { Text(text = "Telefone") },
                modifier = Modifier
                    .width(190.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(text = validaTelefone(dados.telefone))
        }
    }
    Spacer(modifier = Modifier.height(40.dp))
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .width(280.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF809CBE))
    ) {
        Text(text = "CADASTRAR")
    }

}

data class Endereco(
    var logradouro: String,
    var bairro: String,
    var localidade: String,
    var uf: String,
    var ddd: String,
    val erro: Boolean
)

data class DadosPessoais(
    var nome: String,
    var email: String,
    var cep: String,
    var telefone: String,
    var numero: String,
    var complemento: String
)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileCadastroTheme {
        App()
    }
}