package com.cursoimpacta.mobilecadastro

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.cursoimpacta.mobilecadastro.ui.theme.MobileCadastroTheme
import kotlinx.coroutines.launch
import retrofit2.Response

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

@Composable
fun App() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        CampoUsuario()
    }

}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CampoUsuario() {
    val api = remember { ViaCepService.create() }
    val coroutineScope = rememberCoroutineScope()
    var dados by remember {
        mutableStateOf<DadosPessoais?>(null)
    }
    var cep by remember {
        mutableStateOf("")
    }
    var infoCep by remember {
        mutableStateOf<Endereco?>(null)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        TextField(
            value = dados?.nome ?: "",
            onValueChange = { dados?.nome = it },
            label = { Text(text = "Nome") })

        TextField(
            value = dados?.email ?: "",
            onValueChange = { dados?.email = it },
            label = { Text(text = "Email") })

        TextField(value = cep, onValueChange = {
            if (it.isDigitsOnly()) {
                cep = it
            }
        }, label = { Text(text = "CEP") })

        if (cep.length == 8) {
            coroutineScope.launch {
                lateinit var response: Response<Endereco>
                response = api.procuraCep(cep)
                infoCep = if (response.isSuccessful && !response.body()!!.erro) {
                    response.body()
                } else {
                    null
                }
            }
            if (infoCep == null) {
                Text(text = "CEP inválido")
            }
        }

        TextField(
            value = infoCep?.logradouro ?: "",
            onValueChange = { infoCep?.logradouro = it },
            label = {
                Text(
                    text = "Logradouro"
                )
            })
        TextField(value = infoCep?.bairro ?: "", onValueChange = { infoCep?.bairro = it }, label = {
            Text(
                text = "Bairro"
            )
        })
        Row {
            TextField(
                value = dados?.numero ?: "",
                onValueChange = { dados?.numero = it },
                label = {
                    Text(
                        text = "Número"
                    )
                },
                modifier = Modifier.width(150.dp)
            )
            TextField(
                value = dados?.complemento ?: "",
                onValueChange = { dados?.complemento = it },
                label = {
                    Text(
                        text = "Complemento"
                    )
                },
                modifier = Modifier.width(150.dp)
            )
        }
        Row {
            TextField(
                value = infoCep?.localidade ?: "",
                onValueChange = { infoCep?.localidade = it },
                label = {
                    Text(
                        text = "Cidade"
                    )
                })
            TextField(
                value = infoCep?.uf ?: "",
                onValueChange = { infoCep?.uf = it },
                label = {
                    Text(
                        text = "Estado"
                    )
                })

            Row {
                TextField(
                    value = infoCep?.ddd ?: "",
                    onValueChange = { infoCep?.ddd = it },
                    label = { Text(text = "Nome") })
                TextField(
                    value = dados?.telefone ?: "",
                    onValueChange = { dados?.telefone = it },
                    label = { Text(text = "Telefone") })
            }
        }


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