package com.example.pokedex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedex.adapter.PokemonAdapter
import com.example.pokedex.model.Pokemon
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R


import com.example.pokedex.data.PokemonRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewPokemonList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        PokemonRepository.getPokemon(1) { pokemon ->
            pokemon?.let {
                recyclerView.adapter = PokemonAdapter(listOf(it))
            }
        }
    }
}

