package com.example.pokedex.network

import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.PokemonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Define los servicios web de la API Pokemon
interface PokemonService {
    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id: Int): Call<Pokemon>

    @GET("pokemon")
    fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<PokemonResponse>
}

