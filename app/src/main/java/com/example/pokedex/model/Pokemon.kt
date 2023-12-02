package com.example.pokedex.model

data class Pokemon(
    val id: Int,
    val name: String,
    var sprites: Sprites
)

data class Sprites(
    val front_default: String?
)