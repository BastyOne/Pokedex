package com.example.pokedex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val clickListener: (Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>(), Filterable {

    // Lista de pokémones que se mostrarán en el adaptador
    var pokemonFilterList = mutableListOf<Pokemon>()

    init {
        // Inicializa la lista filtrada con todos los pokémones al crear el adaptador
        pokemonFilterList = pokemonList
    }

    // Crear una nueva vista (invocado por el layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view)
    }

    // Reemplaza el contenido de una vista
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonFilterList[position]

        // Configura el contenido del ViewHolder con los datos del Pokémon
        holder.textViewName.text = pokemon.name
        holder.textViewId.text = pokemon.id.toString()

        // Cargar imagen usando Glide con un placeholder y manejo de errores
        Glide.with(holder.itemView.context)
            .load(pokemon.sprites.front_default)
            .placeholder(R.drawable.poke_ball)
            .error(R.drawable.error)
            .into(holder.imageViewSprite)

        // Establece un click listener para cada elemento de la lista
        holder.itemView.setOnClickListener {
            clickListener(pokemon)
        }
    }

    // Devuelve el tamaño de la lista de pokémones filtrada
    override fun getItemCount() = pokemonFilterList.size

    // Agrega nuevos pokémones a la lista y notifica al adaptador para actualizar la UI
    fun addPokemons(newPokemons: List<Pokemon>) {
        val startPosition = pokemonList.size
        pokemonList.addAll(newPokemons)
        if (pokemonFilterList.size == startPosition) {
            pokemonFilterList.addAll(newPokemons)
        }
        notifyDataSetChanged()
    }

    // ViewHolder proporciona una referencia directa a cada uno de los views dentro de un elemento de datos
    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewSprite: ImageView = view.findViewById(R.id.imageViewSprite)
        val textViewName: TextView = view.findViewById(R.id.textViewName)
        val textViewId: TextView = view.findViewById(R.id.textViewId)
    }

    // Lógica de filtrado para la búsqueda de pokémones
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

            // Actualiza la lista filtrada y notificar cambios al adaptador
            @SuppressWarnings("unchecked")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                pokemonFilterList = results?.values as MutableList<Pokemon>
                notifyDataSetChanged()
            }
        }
    }
}
