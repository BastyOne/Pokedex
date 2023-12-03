package com.example.pokedex.model

// Representa la respuesta de la API al solicitar la lista
data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonSummary>
)

data class PokemonSummary(
    val name: String,
    val url: String,
)