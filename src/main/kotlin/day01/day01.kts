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

val finalPosition2 = processDirections(moves, true)
val distance2 = manhattanDistance(Point(), finalPosition2)
println("Part 2, distance to first collision: $distance2")
