package com.example.pokedex.model

data class Pokemon(
    val id: Int,
    val name: String,
    var sprites: Sprites,
    var height: Int?,
    var weight: Int?,
    var types: List<Type>?,
    var abilities: List<Ability>?
)

data class Sprites(
    val front_default: String?
)
data class Type(
    val type: TypeDetail
)

data class TypeDetail(
    val name: String
)

data class Ability(
    val ability: AbilityDetail
)

data class AbilityDetail(
    val name: String
)