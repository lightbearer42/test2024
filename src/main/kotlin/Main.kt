package com.vasilevskii

import com.vasilevskii.dto.Clue
import com.vasilevskii.dto.HouseResult
import com.vasilevskii.dto.Puzzle
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import zebra.*
import java.io.FileInputStream
import java.io.FileOutputStream

private val json = Json {
    ignoreUnknownKeys = true
    classDiscriminator = "type"
}

@OptIn(ExperimentalSerializationApi::class)
fun main(args: Array<String>) {
    if (args.size < 2) throw IllegalArgumentException("Not enough arguments")

    val puzzle = json.decodeFromStream<Puzzle>(FileInputStream(args[0]))
    val constraints = try {
        puzzle.clues.map {
            when (it) {
                is Clue.SimpleClue -> SimpleConstraint(it.argument.first(), it.sentence.first())
                is Clue.NumberClue -> NumberConstraint(it.house - 1, it.argument.first())
                is Clue.NeighbourClue -> mapNeighbourConstraint(it)
            }
        }
    } catch (e: NoSuchElementException) {
        error("Arguments and sentences can't be empty!")
    }
    val assignment = Zebra(puzzle.variables, constraints).find()
    val output = assignment.map { house ->
        HouseResult(house.number + 1, house.variables.mapValues { it.value.toString() })
    }
    json.encodeToStream(output, FileOutputStream(args[1]))
}

fun mapNeighbourConstraint(neighbourClue: Clue.NeighbourClue): Constraint {
    return if (neighbourClue.left == neighbourClue.right) {
        AnySideNeighbourConstraint(
            neighbourClue.offset,
            neighbourClue.argument.first(),
            neighbourClue.sentence.first()
        )
    } else {
        val offset = if (neighbourClue.left) {
            -neighbourClue.offset
        } else {
            neighbourClue.offset
        }
        ExactSideNeighbourConstraint(
            offset,
            neighbourClue.argument.first(),
            neighbourClue.sentence.first()
        )
    }

}

fun <K, V> Map<K, V>.first(): Pair<K, V> {
    return this.map { it.key to it.value }.first()
}
