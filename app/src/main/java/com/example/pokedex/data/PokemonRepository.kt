package com.example.pokedex.data

import com.example.pokedex.model.Pokemon
import com.example.pokedex.network.PokemonService
import com.example.pokedex.network.RetrofitClient

object PokemonRepository {
    private val pokemonService = RetrofitClient.instance.create(PokemonService::class.java)

    fun getPokemon(id: Int, callback: (Pokemon?) -> Unit) {
        val call = pokemonService.getPokemon(id)
        call.enqueue(object : retrofit2.Callback<Pokemon> {
            override fun onResponse(call: retrofit2.Call<Pokemon>, response: retrofit2.Response<Pokemon>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: retrofit2.Call<Pokemon>, t: Throwable) {
                callback(null)
            }
        })
    }
}
