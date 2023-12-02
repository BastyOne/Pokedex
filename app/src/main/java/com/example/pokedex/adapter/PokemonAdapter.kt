package com.example.pokedex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.model.Pokemon
import java.util.Locale

class PokemonAdapter(
    private val pokemonList: MutableList<Pokemon>,
    private val clickListener: (Pokemon) -> Unit) :
    RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>(), Filterable {

    var pokemonFilterList = mutableListOf<Pokemon>()

    init {
        pokemonFilterList = pokemonList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonFilterList[position]
        holder.textViewName.text = pokemon.name
        holder.textViewId.text = pokemon.id.toString()

        Glide.with(holder.itemView.context)
            .load(pokemon.sprites.front_default)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error)
            .into(holder.imageViewSprite)

        holder.itemView.setOnClickListener {
            clickListener(pokemon)
        }
    }

    override fun getItemCount() = pokemonFilterList.size

    fun addPokemons(newPokemons: List<Pokemon>) {
        val startPosition = pokemonList.size
        pokemonList.addAll(newPokemons)
        if (pokemonFilterList.size == startPosition) {
            pokemonFilterList.addAll(newPokemons)
        }
        notifyDataSetChanged()
    }

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewSprite: ImageView = view.findViewById(R.id.imageViewSprite)
        val textViewName: TextView = view.findViewById(R.id.textViewName)
        val textViewId: TextView = view.findViewById(R.id.textViewId)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                pokemonFilterList = if (charSearch.isEmpty()) {
                    pokemonList
                } else {
                    val resultList = mutableListOf<Pokemon>()
                    for (row in pokemonList) {
                        if (row.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = pokemonFilterList
                return filterResults
            }
            fun updateFilteredList(newList: List<Pokemon>) {
                pokemonFilterList.clear()
                pokemonFilterList.addAll(newList)
                notifyDataSetChanged()
            }


            @SuppressWarnings("unchecked")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                pokemonFilterList = results?.values as MutableList<Pokemon>
                notifyDataSetChanged()
            }
        }
    }
}