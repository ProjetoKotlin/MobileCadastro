package com.cursoimpacta.mobilecadastro

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cursoimpacta.mobilecadastro.ui.theme.MobileCadastroTheme
class MainActivity : ComponentActivity() {
    // dbHelper -> usado para interagir com o banco de dados
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

// O componente que será inicializado primeiro é a TelaCadastro com os formularios a serem preenchidos
@Composable
fun TelaPrincipal(db: Database) {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var telaCadastro by remember { mutableStateOf(true) }
    val onBitmapCaptured: (Bitmap?) -> Unit = { bitmap ->
        imageBitmap = bitmap
    }

    Column(
        modifier = Modifier
            .background(color = Color(0xFFD1E0F2))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        // Exibição do Header com as duas opções de clique - tela cadastro ou tela de buscarUsuario, as duas funções recebem db
        Header(telaCadastro, onMudarTela = { telaCadastro = it })

        if (telaCadastro) {
            TelaCadastro(db, onBitmapCaptured)

        } else {
            TelaBuscarUsuario(db, imageBitmap, onBitmapCaptured)
        }
    }
}

@Composable
fun TelaCadastro(db: Database, onBitmapCaptured: (Bitmap?) -> Unit) {
    var dados by remember {
        mutableStateOf(DadosPessoais("", "", "", "9", "", ""))
    }
    var btnCadastrarClicked by remember {
        mutableStateOf(false)
    }
    var infoCep by remember { mutableStateOf(Endereco("", "", "", "", "", false)) }

    // Variável auxiliar para mostrar a imagem capturada
    var imageBitmapToShow by remember { mutableStateOf<Bitmap?>(null) }

    Spacer(modifier = Modifier.height(30.dp))

    Camera(onBitmapValor = { bitmap ->
        onBitmapCaptured(bitmap)
        imageBitmapToShow = bitmap  // Atualiza a imagem para ser mostrada
    })

    if (imageBitmapToShow != null) {
        Image(
            bitmap = imageBitmapToShow!!.asImageBitmap(),
            contentDescription = "Captured image",
            modifier = Modifier
                .size(300.dp),
        )
        Spacer(modifier = Modifier.height(20.dp))
    }

    Formulario(
        dados = dados,
        onDadosChange = { dados = it },
        infoCep = infoCep,
        onInfoCepChange = { infoCep = it }
    )

    Button(
        onClick = {
            db.addUser(dados, infoCep)
            dados = DadosPessoais("", "", "", "9", "", "")
            infoCep = Endereco("", "", "", "", "", false)
            btnCadastrarClicked = true
            // Limpar a imagem capturada após o cadastro
            imageBitmapToShow = null  // Limpa a imagem exibida
        },
        modifier = Modifier
            .width(280.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF809CBE))
    ) {
        Text(text = "CADASTRAR")
    }

    // Mensagem de sucesso em caso de cadastro realizado com sucesso
    if (btnCadastrarClicked) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Dados cadastrados com sucesso!")
    }
    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
fun TelaBuscarUsuario(db: Database, imageBitmap: Bitmap?, onBitmapCaptured: (Bitmap?) -> Unit) {
    var dados by remember { mutableStateOf(DadosPessoais("", "", "", "9", "", "")) }
    var infoCep by remember { mutableStateOf(Endereco("", "", "", "", "", false)) }
    var entrada by remember { mutableStateOf("") }
    var btnBuscarClicked by remember { mutableStateOf(false) }

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
         e o campo de busca para tentar novamente buscar a informação com outro nome

         Caso ao contrario ele entra no if e exibe o perfil do usuario com os dados que foram
         pesquisados no campo de busca, dessa forma será possivel editar ou deletar
         os dados quando quiser.
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
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap.asImageBitmap(),
                            contentDescription = "Captured image",
                            modifier = Modifier
                                .size(300.dp),  // Ajuste do tamanho da imagem
                            contentScale = ContentScale.FillWidth
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    ExibirDados(dados, infoCep)
                } else {
                    Camera(onBitmapValor = { bitmap ->
                        onBitmapCaptured(bitmap)
                    })
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap.asImageBitmap(),
                            contentDescription = "Captured image",
                            modifier = Modifier
                                .size(300.dp),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
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