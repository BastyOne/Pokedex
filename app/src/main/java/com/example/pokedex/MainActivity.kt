package com.example.pokedex
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.example.pokedex.adapter.PokemonAdapter
import com.example.pokedex.data.PokemonRepository
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
        pokemonAdapter = PokemonAdapter(mutableListOf())
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
    private fun loadMorePokemons() {
        PokemonRepository.getPokemonList(limit, offset) { pokemonList ->
            pokemonList?.let {
                pokemonAdapter.addPokemons(it)
                it.forEach { pokemon ->
                    PokemonRepository.getPokemonDetails(pokemon.id) { sprites ->
                        val index = pokemonList.indexOfFirst { it.id == pokemon.id }
                        if (index != -1) {
                            pokemonList[index].sprites = sprites ?: Sprites(front_default = null)
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