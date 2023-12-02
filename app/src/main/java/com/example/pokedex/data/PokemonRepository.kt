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
    private val pokemonService = RetrofitClient.instance.create(PokemonService::class.java)

    fun getPokemonList(limit: Int, offset: Int, callback: (List<Pokemon>?) -> Unit) {
        val call = pokemonService.getPokemonList(limit, offset)
        call.enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    val pokemonList = response.body()?.results?.map { summary ->
                        Pokemon(id = extractIdFromUrl(summary.url), name = summary.name, sprites = Sprites(front_default = null))
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


    fun getPokemonDetails(id: Int, callback: (Sprites?) -> Unit) {
        val call = pokemonService.getPokemon(id)
        call.enqueue(object : Callback<Pokemon> {
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                if (response.isSuccessful) {
                    callback(response.body()?.sprites)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                callback(null)
            }
        })
    }


    private fun extractIdFromUrl(url: String): Int {
        return url.dropLast(1).takeLastWhile { it.isDigit() }.toInt()
    }
}