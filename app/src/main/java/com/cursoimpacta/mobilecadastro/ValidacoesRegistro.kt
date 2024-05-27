package com.cursoimpacta.mobilecadastro

// função tem como proposta validar se o nome tem mais de 2 palavras e se cada nome possui mais de 3 letras
fun validaNome(nome: String): String {
    val lista = nome.split(" ")
    if (lista.size >= 2) {
        for (palavra in lista) {
            if (palavra.length < 3) {
                return "Cada nome deve conter no mínimo 3 letras cada!"
            }
        }
        return "Correto"
    } else {
        return "O nome precisa de no mínimo 2 palavras!"
    }
}

fun validaEmail(email: String): String {
    val regex = Regex("[A-Z|a-z|0-9|\\.|_|-]+@[\\w-]+\\.[a-z]{2,}") // Regex com as limitações das características aceitas no momento da escrita do e-mail
    if (!regex.matches(email)){
        return "Email inválido"
    }
    return "Correto"

}

// Validar se o ddd do telefone contém 2 dígitos e se o número de telefone começa com 9
// e possui 9 dígitos, totalizando 2 + 9 = 11 ao todo
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
