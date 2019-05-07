package day01

import java.io.File

val lines = mutableListOf<String>()
File("input_d1.txt").forEachLine {
    lines.add(it)
}

val moves = mutableListOf<String>()
lines.forEach {
    it.split(Regex(",")).forEach {
        moves.add(it.trim())
    }
}


val finalPosition = processDirections(moves)
val distance = manhattanDistance(Point(), finalPosition)

println("Part 1, distance to intersection: $distance")