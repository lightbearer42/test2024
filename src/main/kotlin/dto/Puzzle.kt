package com.vasilevskii.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Puzzle(
    val variables: Map<String, List<String>>,
    val clues: List<Clue> = emptyList()
)

@Serializable
sealed class Clue {
    abstract val argument: Map<String, String>

    @Serializable
    @SerialName("simple")
    data class SimpleClue(
        override val argument: Map<String, String>,
        val sentence: Map<String, String>
    ) : Clue()

    @Serializable
    @SerialName("number")
    data class NumberClue(
        override val argument: Map<String, String>,
        val house: Int
    ) : Clue()

    @Serializable
    @SerialName("neighbour")
    data class NeighbourClue(
        override val argument: Map<String, String>,
        val sentence: Map<String, String>,
        val offset: Int = 1,
        val right: Boolean = false,
        val left: Boolean = false
    ) : Clue()
}

@Serializable
data class HouseResult(
    val number: Int,
    val variables: Map<String, String>
)