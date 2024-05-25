package com.cursoimpacta.mobilecadastro
// using SendGrid's Java Library
// https://github.com/sendgrid/sendgrid-java
import okhttp3.*
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

fun main() {
    val client = OkHttpClient()

    val apiKey = "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"  // Substitua pela sua API key do Sendinblue
    val url = "https://api.sendinblue.com/v3/smtp/email"

    val json = """
    {
      "sender": {
        "name": "Guilherme",
        "email": "756e16001@smtp-brevo.com"
      },
      "to": [
        {
          "email": "guil.sdom@icloud.com",
          "name": "Guilherme"
        }
      ],
      "subject": "Hello, World!",
      "htmlContent": "<html><body><p>Hello, this is a test email sent from Kotlin using Sendinblue.</p></body></html>"
    }
    """.trimIndent()

    val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    val request = Request.Builder()
        .url(url)
        .post(body)
        .addHeader("api-key", apiKey)
        .addHeader("Content-Type", "application/json")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println(response.body?.string())
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
        return ""
    } else {
        return "O nome precisa de no mínimo 2 palavras!"
    }
}

fun validaEmail(email: String): String {
    val regex = Regex("[A-Z|a-z|0-9|\\.|_|-]+@[\\w-]+\\.[a-z]{2,}")
    if (!regex.matches(email)){
        return "Email inválido"
    }
    return ""

}

fun validaTelefone(ddd: String, telefone: String): String {
    var mensagem = ""
    if (ddd.length != 2) {
        mensagem += "O DDD deve conter 2 dígitos!\n"
    }
    if (!telefone.startsWith("9")){
        mensagem += "O número de telefone deve começar com o número 9!\n"
    }
    else if (telefone.length != 9){
        mensagem += "O número de telefone deve conter nove dígitos!"
    }
    return mensagem
}
