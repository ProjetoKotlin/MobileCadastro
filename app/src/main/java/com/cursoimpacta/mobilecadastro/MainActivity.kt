package com.cursoimpacta.mobilecadastro

import android.graphics.Paint.Style
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cursoimpacta.mobilecadastro.ui.theme.MobileCadastroTheme


class MainActivity : ComponentActivity() {
    // dbHelper usada para interagir com o banco de dados.
    private lateinit var dbHelper: Database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileCadastroTheme {
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

// componente que eserá inicializado primeiro é a telaCadastro com os formularios a serem preenchido 
@Composable
fun TelaPrincipal(db: Database) {
    var telaCadastro by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .background(color = Color(0xFFD1E0F2))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        //exibição do menu exibindo as duas opçãoes de clique a tela cadastro ou tela de buscarUsuario, as duas recebe db
        Header(telaCadastro, onMudarTela = { telaCadastro = it })
        if (telaCadastro) {
            TelaCadastro(db)

        } else {
            TelaBuscarUsuario(db)
        }
    }
}


@Composable
fun TelaCadastro(db: Database) {
    var dados by remember {
        mutableStateOf(DadosPessoais("", "", "", "9", "", ""))
    }
    var btnCadastrarClicked by remember {
        mutableStateOf(false)
    }
    var infoCep by remember { mutableStateOf(Endereco("", "", "", "", "", false)) }

    Spacer(modifier = Modifier.height(30.dp))
    Formulario(dados = dados,
        onDadosChange = { dados = it },
        infoCep = infoCep,
        onInfoCepChange = { infoCep = it })
    /*
     botõao exibido ele adicona os dados que foi gerado e após os dados
     salvos os campos recebe uma string vazia, para limpar os campos
     e permitindo assim o preenchimento de novas informações. 
    */
    Button(
        onClick = {
            db.addUser(dados, infoCep)
            dados = DadosPessoais("","","","","","")
            infoCep = Endereco("", "", "", "", "", false)
            btnCadastrarClicked = true
        },
        modifier = Modifier
            .width(280.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF809CBE))
    ) {
        Text(text = "CADASTRAR")
    }

    // mensagem de sucesso em caso de cadastro realizado com sucesso
    if (btnCadastrarClicked){
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Dados cadastrados com sucesso!")
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun TelaBuscarUsuario(db: Database) {
    var dados by remember { mutableStateOf(DadosPessoais("", "", "", "9", "", "")) }
    var infoCep by remember { mutableStateOf(Endereco("", "", "", "", "", false)) }
    var entrada by remember {
        mutableStateOf("")
    }
    var btnBuscarClicked by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .background(color = Color(0xFFD1E0F2))
            .width(310.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Buscar usuário:", fontSize = 18.sp, modifier = Modifier
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row {
            TextField(
                value = entrada,
                modifier = Modifier.width(225.dp),
                onValueChange = { entrada = it },
                label = { Text(text = "Digite o nome: ") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                ),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                onClick = {
                    val (dadosResp, infoCepResp) = db.getUserByName(entrada)
                    dados = dadosResp
                    infoCep = infoCepResp
                    btnBuscarClicked = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF809CBE)),
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "⌕", fontSize = 26.sp)
            }
        }
    }
    Spacer(modifier = Modifier.height(30.dp))

    /*
         Em caso da procura de um nome que não foi cadastrado na base e do banco de dados. 
         Criamos essa função que resgata o retorno do banco de dados informando com a tratativa de mensagem "Não encontrado". 
         Ela evita disparo da  tela completa do perfil do usuario, e exibindo assim a mensagem de não encontrato 
         e o campo de busca para tentar novamente buscar informação com outro nome
         
         Caso ao contrario ele entra no if e exibe o perfil do usuario com o dados que foram 
         pesquisado no campo de busca, dessa forma ele será possivél editar ou deletar
         o dados quando quiser.
    */
    
    if (btnBuscarClicked) {
        if (dados.nome != "Não encontrado") {
            var btnAlterarClicked by remember {
                mutableStateOf(false)
            }
            var btnDeletarClicked by remember {
                mutableStateOf(false)
            }

            PerfilEncontrado(
                { btnAlterarClicked = it },
                { btnDeletarClicked = it }
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (btnDeletarClicked) {
                db.deleteUser(entrada)
                Text(text = "Dados deletados com sucesso!")
            } else {
                if (!btnAlterarClicked) {
                    ExibirDados(dados, infoCep)
                } else {
                    Formulario(
                        dados = dados,
                        onDadosChange = { dados = it },
                        infoCep = infoCep,
                        onInfoCepChange = { infoCep = it })

                    Button(
                        onClick = {
                            db.updateUser(dados, infoCep)
                            btnBuscarClicked = false
                        },
                        modifier = Modifier
                            .width(280.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF809CBE))
                    ) {
                        Text(text = "SALVAR")
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        } else {
            Text(text = "Usuário não encontrado!")
        }
    }
}

// função para somente exibir os dados como texto na tela de perfil do usuario
@Composable
fun ExibirDados(dados: DadosPessoais, endereco: Endereco) {
    val dadosTexto = """
        Nome: ${dados.nome}
        Email: ${dados.email}
        CEP: ${dados.cep}
        Telefone: ${dados.telefone}
        Número: ${dados.numero}
        Complemento: ${dados.complemento}
        
        Logradouro: ${endereco.logradouro}
        Bairro: ${endereco.bairro}
        Localidade: ${endereco.localidade}
        UF: ${endereco.uf}
        DDD: ${endereco.ddd}
    """.trimIndent()

    Text(text = dadosTexto, modifier = Modifier.width(310.dp))
}

