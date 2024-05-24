package com.cursoimpacta.mobilecadastro

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {
    @GET("{cep}/json/")
    suspend fun procuraCep(@Path("cep") cep: String): Response<Endereco>

    companion object {
        fun create(): ViaCepService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ViaCepService::class.java)
        }
    }
}