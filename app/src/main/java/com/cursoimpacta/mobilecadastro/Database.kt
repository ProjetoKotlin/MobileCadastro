package com.cursoimpacta.mobilecadastro

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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



class Database(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "cadastro.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "DadosCadastrais"
        const val COLUMN_ID = "id"
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
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NOME TEXT,
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

    fun addUser(dadosPessoais: DadosPessoais, endereco: Endereco) {
        val db = this.writableDatabase
        val values = ContentValues()

        // Dados Pessoais
        values.put(COLUMN_NOME, dadosPessoais.nome)
        values.put(COLUMN_EMAIL, dadosPessoais.email)
        values.put(COLUMN_CEP, dadosPessoais.cep)
        values.put(COLUMN_NUMERO, dadosPessoais.numero)
        values.put(COLUMN_COMPLEMENTO, dadosPessoais.complemento)
        values.put(COLUMN_TELEFONE, dadosPessoais.telefone)

        // Endereço
        values.put(COLUMN_LOGRADOURO, endereco.logradouro)
        values.put(COLUMN_BAIRRO, endereco.bairro)
        values.put(COLUMN_LOCALIDADE, endereco.localidade)
        values.put(COLUMN_UF, endereco.uf)
        values.put(COLUMN_DDD, endereco.ddd)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllUsers(): Pair<List<DadosPessoais>, List<Endereco>> {
        val listaDadosPessoais = mutableListOf<DadosPessoais>()
        val listaEndereco = mutableListOf<Endereco>()
        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
                val cep = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CEP))
                val telefone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONE))
                val numero = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUMERO))
                val complemento = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPLEMENTO))

                // Recuperar dados de endereço
                val logradouro = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOGRADOURO))
                val bairro = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BAIRRO))
                val localidade = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCALIDADE))
                val uf = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UF))
                val ddd = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DDD))

                val dadosPessoais = DadosPessoais(nome, email, cep, telefone, numero, complemento)
                val endereco = Endereco(logradouro, bairro, localidade, uf, ddd, false)

                listaDadosPessoais.add(dadosPessoais)
                listaEndereco.add(endereco)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return Pair(listaDadosPessoais, listaEndereco)
    }
}
//    fun updateUser(task: Task) {
//        val db = this.writableDatabase
//        val values = ContentValues()
//        values.put(COLUMN_TITLE, task.title)
//        values.put(COLUMN_DESCRIPTION, task.descripton)
//
//        db.update(TABLE_NAME, values, "$COLUMN_ID=?"
//            , arrayOf((task.id.toString())))
//
//        db.close()
//    }
//
//    fun deleteUser(taskId: Long) {
//        val db = this.writableDatabase
//        db.delete(TABLE_NAME, "$COLUMN_ID=?"
//            , arrayOf(taskId.toString()))
//        db.close()
//    }