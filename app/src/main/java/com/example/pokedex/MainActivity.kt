package com.example.pokedex
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.bumptech.glide.Glide
import com.example.pokedex.adapter.PokemonAdapter
import com.example.pokedex.data.PokemonRepository
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.Sprites

class MainActivity : AppCompatActivity() {
    private lateinit var pokemonAdapter: PokemonAdapter
    private var isLoading = false
    private var offset = 0
    private val limit = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewPokemonList)
        val searchView: SearchView = findViewById(R.id.searchView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        pokemonAdapter = PokemonAdapter(mutableListOf()){
            pokemon -> showPokemonDetailsDialog(pokemon)
        }
        recyclerView.adapter = pokemonAdapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= (lastVisibleItem + 5)) {
                    loadMorePokemons()
                    isLoading = true
                }
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                pokemonAdapter.filter.filter(newText)
                return false
            }
        })

        loadMorePokemons()
    }

    private fun showPokemonDetailsDialog(pokemon: Pokemon) {

        val dialogView = layoutInflater.inflate(R.layout.pokemon_details, null)
        val imageView: ImageView = dialogView.findViewById(R.id.dialog_pokemon_image)
        val nameTextView: TextView = dialogView.findViewById(R.id.dialog_pokemon_name)
        val heightTextView: TextView = dialogView.findViewById(R.id.dialog_pokemon_height)
        val weightTextView: TextView = dialogView.findViewById(R.id.dialog_pokemon_weight)
        val typesTextView: TextView = dialogView.findViewById(R.id.dialog_pokemon_types)
        val abilitiesTextView: TextView = dialogView.findViewById(R.id.dialog_pokemon_abilities)


        nameTextView.text = pokemon.name
        heightTextView.text = getString(R.string.pokemon_height, pokemon.height)
        weightTextView.text = getString(R.string.pokemon_weight, pokemon.weight)

        // Concatena los nombres de los tipos
        val types = pokemon.types?.joinToString(", ") { it.type.name }
        typesTextView.text = getString(R.string.pokemon_types, types)

        // Concatena los nombres de las habilidades
        val abilities = pokemon?.abilities?.joinToString(", ") { it.ability.name }
        abilitiesTextView.text = getString(R.string.pokemon_abilities, abilities)


        Glide.with(this).load(pokemon.sprites.front_default).into(imageView)

        // Crea y muestra el diálogo
        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun loadMorePokemons() {
        PokemonRepository.getPokemonList(limit, offset) { pokemonList ->
            pokemonList?.let {
                pokemonAdapter.addPokemons(it)
                it.forEach { pokemon ->
                    PokemonRepository.getPokemonDetails(pokemon.id) { pokemonDetails ->
                        val index = pokemonList.indexOfFirst { it.id == pokemon.id }
                        if (index != -1 && pokemonDetails != null) {
                            // Actualiza los detalles del Pokémon en la lista
                            pokemonList[index].sprites = pokemonDetails.sprites
                            pokemonList[index].height = pokemonDetails.height
                            pokemonList[index].weight = pokemonDetails.weight
                            pokemonList[index].types = pokemonDetails.types
                            pokemonList[index].abilities = pokemonDetails.abilities

                            pokemonAdapter.notifyItemChanged(index)
                        }
                    }
                }
                offset += it.size
                isLoading = false
            }
        }
    }
}