package com.example.pokedex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.model.Pokemon

class PokemonAdapter(private val pokemonList: List<Pokemon>) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.textViewName.text = pokemon.name
        Glide.with(holder.itemView.context)
            .load(pokemon.sprites.front_default)
            .into(holder.imageViewSprite)
    }

    override fun getItemCount() = pokemonList.size

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewSprite: ImageView = view.findViewById(R.id.imageViewSprite)
        val textViewName: TextView = view.findViewById(R.id.textViewName)
    }
}
