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

class PokemonAdapter(private val pokemonList: MutableList<Pokemon>) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.textViewName.text = pokemon.name
        holder.textViewId.text = pokemon.id.toString()


        pokemon.sprites.front_default?.let { imageUrl ->
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error)
                .into(holder.imageViewSprite)
        } ?: holder.imageViewSprite.setImageResource(R.drawable.placeholder_image)
    }


    override fun getItemCount() = pokemonList.size

    fun addPokemons(newPokemons: List<Pokemon>) {
        val startPosition = this.pokemonList.size
        this.pokemonList.addAll(newPokemons)
        notifyItemRangeInserted(startPosition, newPokemons.size)
    }

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewSprite: ImageView = view.findViewById(R.id.imageViewSprite)
        val textViewName: TextView = view.findViewById(R.id.textViewName)
        val textViewId: TextView = view.findViewById(R.id.textViewId)
    }
}
