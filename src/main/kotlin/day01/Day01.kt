package day01

import kotlin.math.abs

fun processDirections(moves: List<String>): Point {
    var position = Point()
    var direction = Direction.NORTH

    moves.forEach {
        val turn = it.take(1)
        val distance = it.takeLast(it.length - 1)
        direction = direction.turn(Turn.from(turn))
        position = position.move(direction, distance.toInt())
    }

    return position
}

fun manhattanDistance(from: Point, to: Point): Int {
    return abs(from.x - to.x) + abs(from.y - to.y)
}