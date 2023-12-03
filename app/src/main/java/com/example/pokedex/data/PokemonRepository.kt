package com.example.pokedex.data

import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.PokemonResponse
import com.example.pokedex.model.Sprites
import com.example.pokedex.network.PokemonService
import com.example.pokedex.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PokemonRepository {
    private val pokemonService = RetrofitClient.instance.create(PokemonService::class.java) // Inicialización del servicio para hacer llamadas de red con Retrofit

    // Funcion que obtiene una lista de pokemones desde la API
    fun getPokemonList(limit: Int, offset: Int, callback: (List<Pokemon>?) -> Unit) {
        val call = pokemonService.getPokemonList(limit, offset) //lamada de red asincronica para obtener Pokemon
        call.enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    // Mapea la respuesta a una lista de objetos Pokemon
                    val pokemonList = response.body()?.results?.map { summary ->
                        Pokemon(id = extractIdFromUrl(summary.url), name = summary.name, sprites = Sprites(front_default = null), height = 0, weight = 0, types = emptyList(), abilities = emptyList())
                    }
                    callback(pokemonList)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun getPokemonDetails(id: Int, callback: (Pokemon?) -> Unit) { // Obtiene detalles de pokemones
        val call = pokemonService.getPokemon(id)
        call.enqueue(object : Callback<Pokemon> {
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                // Devuelve los detalles del pokemon si la respuesta es exitosa
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                callback(null)
            }
        })
    }

    // Funcion auxiliar para extraer el ID del pokemon de la URL
    private fun extractIdFromUrl(url: String): Int {
        return url.dropLast(1).takeLastWhile { it.isDigit() }.toInt()
    }
}