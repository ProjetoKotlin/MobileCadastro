package com.cursoimpacta.mobilecadastro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import kotlinx.coroutines.launch
import retrofit2.Response

@Composable
fun Formulario(
    /*
    foram criados essa variavél interação dos dados entre as telas, 
    por se tratar de componente precisariamos que os dados fosse dinamicos e 
    exibir somente quando forem  realizado ações de editar
    */
    dados: DadosPessoais,
    onDadosChange: (DadosPessoais) -> Unit,
    infoCep: Endereco,
    onInfoCepChange: (Endereco) -> Unit,
) {
    val api = remember { ViaCepService.create() }
    val coroutineScope = rememberCoroutineScope()

    var msgStatus by remember {
        mutableStateOf("")
    }
    val colorWhite = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.White,
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .width(280.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        TextField(
            value = dados.nome,
            { onDadosChange(dados.copy(nome = it)) },
            Modifier.clip(RoundedCornerShape(8.dp)),
            label = { Text(text = "Nome") },
            colors = colorWhite
        )

        // Nesse campo estamos verificando se dados.nome não está vazio, assim também feito no campo de email
        if (dados.nome.isNotEmpty()) {
            Text(
                text = validaNome(dados.nome), color = Color.Red, modifier = Modifier
                    .align(Alignment.Start)
            )
        }

        TextField(
            value = dados.email,
            onValueChange = { onDadosChange(dados.copy(email = it)) },
            label = { Text(text = "Email") },
            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
            colors = colorWhite
        )
        if (dados.email.isNotEmpty()) {
            Text(
                text = validaEmail(dados.email), color = Color.Red, modifier = Modifier
                    .align(Alignment.Start)
            )
        }

        Text(
            text = "Endereço:",
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            TextField(value = dados.cep, onValueChange = {
                if (it.isDigitsOnly()) {
                    onDadosChange(dados.copy(cep = it))
                }
            }, label = { Text(text = "CEP") },
                modifier = Modifier
                    .width(195.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    if (dados.cep.length == 8) {
                        coroutineScope.launch {
                            msgStatus = "Aguarde..."
                            lateinit var response: Response<Endereco>
                            response = api.procuraCep(dados.cep)
                            if (response.isSuccessful && !response.body()!!.erro) {
                                onInfoCepChange(response.body()!!)
                                msgStatus = ""
                            } else {
                                onInfoCepChange(Endereco("", "", "", "", "", false))
                                msgStatus = "CEP inválido!"
                            }
                        }
                    }

                }, modifier = Modifier
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF809CBE))
            ) {
                Text(text = "⌕", fontSize = 26.sp)
            }
        }
        if (msgStatus != "") {
            Text(text = msgStatus)
        }

        TextField(
            value = infoCep.logradouro,
            onValueChange = { onInfoCepChange(infoCep.copy(logradouro = it)) },
            label = { Text(text = "Logradouro") },
            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
            colors = colorWhite
        )

        TextField(
            value = infoCep.bairro,
            onValueChange = { onInfoCepChange(infoCep.copy(bairro = it)) },
            label = { Text(text = "Bairro") },
            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
            colors = colorWhite
        )

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            TextField(
                value = dados.numero,
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        onDadosChange(dados.copy(numero = it))
                    }
                },
                label = { Text(text = "Número") },
                modifier = Modifier
                    .width(110.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(
                value = dados.complemento,
                onValueChange = { onDadosChange(dados.copy(complemento = it)) },
                label = { Text(text = "Complemento") },
                modifier = Modifier
                    .width(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            TextField(
                value = infoCep.localidade,
                onValueChange = { onInfoCepChange(infoCep.copy(localidade = it)) },
                label = { Text(text = "Cidade") },
                modifier = Modifier
                    .width(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite
            )
            TextField(
                value = infoCep.uf,
                onValueChange = { onInfoCepChange(infoCep.copy(uf = it)) },
                label = { Text(text = "Estado") },
                modifier = Modifier
                    .width(110.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite
            )
        }
        Text(text = "Contato:", modifier = Modifier.align(Alignment.Start), fontSize = 18.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            TextField(
                value = infoCep.ddd,
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        onInfoCepChange(infoCep.copy(ddd = it))
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
                        onDadosChange(dados.copy(telefone = it))
                    }
                },
                label = { Text(text = "Telefone") },
                modifier = Modifier
                    .width(190.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = colorWhite,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        if (infoCep.ddd.isNotEmpty() || dados.telefone != "9") {
            Text(
                text = validaTelefone(infoCep.ddd, dados.telefone),
                modifier = Modifier.align(Alignment.Start),
                color = Color.Red
            )
        }
        Spacer(Modifier.height(12.dp))
    }
}
