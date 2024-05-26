package com.cursoimpacta.mobilecadastro

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class DadosPessoais(
    var nome: String,
    var email: String,
    var cep: String,
    var telefone: String,
    var numero: String,
    var complemento: String
)

data class Endereco(
    var logradouro: String,
    var bairro: String,
    var localidade: String,
    var uf: String,
    var ddd: String,
    val erro: Boolean
)

// Criação do banco de dados e dos métodos CRUD
class Database(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "cadastro.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "DadosCadastrais"
        const val COLUMN_NOME = "nome"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_DDD = "ddd"
        const val COLUMN_TELEFONE = "telefone"
        const val COLUMN_CEP = "cep"
        const val COLUMN_LOGRADOURO = "logradouro"
        const val COLUMN_BAIRRO = "bairro"
        const val COLUMN_NUMERO = "numero"
        const val COLUMN_COMPLEMENTO = "complemento"
        const val COLUMN_LOCALIDADE = "localidade"
        const val COLUMN_UF = "uf"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_NOME TEXT PRIMARY KEY,
            $COLUMN_EMAIL TEXT,
            $COLUMN_DDD TEXT,
            $COLUMN_TELEFONE TEXT,
            $COLUMN_CEP TEXT,
            $COLUMN_LOGRADOURO TEXT,
            $COLUMN_BAIRRO TEXT,
            $COLUMN_NUMERO TEXT,
            $COLUMN_COMPLEMENTO TEXT,
            $COLUMN_LOCALIDADE TEXT,
            $COLUMN_UF TEXT
        )
    """
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVerion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(dados: DadosPessoais, endereco: Endereco) {
        val db = this.writableDatabase
        val values = ContentValues()

        // Dados Pessoais
        values.put(COLUMN_NOME, dados.nome)
        values.put(COLUMN_EMAIL, dados.email)
        values.put(COLUMN_CEP, dados.cep)
        values.put(COLUMN_NUMERO, dados.numero)
        values.put(COLUMN_COMPLEMENTO, dados.complemento)
        values.put(COLUMN_TELEFONE, dados.telefone)

        // Endereço
        values.put(COLUMN_LOGRADOURO, endereco.logradouro)
        values.put(COLUMN_BAIRRO, endereco.bairro)
        values.put(COLUMN_LOCALIDADE, endereco.localidade)
        values.put(COLUMN_UF, endereco.uf)
        values.put(COLUMN_DDD, endereco.ddd)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getUserByName(nome: String): Pair<DadosPessoais, Endereco> {
        val dados = DadosPessoais("", "", "", "", "", "")
        val endereco = Endereco("", "", "", "", "", false)

        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NOME = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(nome))

        if (cursor.moveToFirst()) {
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)) ?: ""
            val cep = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CEP)) ?: ""
            val telefone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONE)) ?: ""
            val numero = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUMERO)) ?: ""
            val complemento =
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPLEMENTO)) ?: ""

            val logradouro = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOGRADOURO)) ?: ""
            val bairro = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BAIRRO)) ?: ""
            val localidade = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCALIDADE)) ?: ""
            val uf = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UF)) ?: ""
            val ddd = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DDD)) ?: ""


            val dadosPessoais = DadosPessoais(nome, email, cep, telefone, numero, complemento)
            val enderecoCompleto = Endereco(logradouro, bairro, localidade, uf, ddd, false)


            cursor.close()
            db.close()
            return Pair(dadosPessoais, enderecoCompleto)
        }

        cursor.close()
        db.close()
        dados.nome = "Não encontrado"
        return Pair(dados, endereco)
    }


    fun updateUser(dados: DadosPessoais, endereco: Endereco) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOME, dados.nome)
            put(COLUMN_EMAIL, dados.email)
            put(COLUMN_DDD, endereco.ddd)
            put(COLUMN_TELEFONE, dados.telefone)
            put(COLUMN_CEP, dados.cep)
            put(COLUMN_LOGRADOURO, endereco.logradouro)
            put(COLUMN_BAIRRO, endereco.bairro)
            put(COLUMN_NUMERO, dados.numero)
            put(COLUMN_COMPLEMENTO, dados.complemento)
            put(COLUMN_LOCALIDADE, endereco.localidade)
            put(COLUMN_UF, endereco.uf)
        }

        db.update(
            TABLE_NAME,
            values,
            "$COLUMN_NOME=?",
            arrayOf(dados.nome)
        )

        db.close()
    }

    fun deleteUser(nome: String) {
        val db = this.writableDatabase
        db.delete(
            TABLE_NAME, "$COLUMN_NOME=?", arrayOf(nome)
        )
        db.close()
    }
}

// Função para somente exibir os dados como texto na tela de perfil do usuário
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
