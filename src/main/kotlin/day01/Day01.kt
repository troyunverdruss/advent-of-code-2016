package day01

import kotlin.math.abs

fun processDirections(moves: List<String>, trackCollisions: Boolean = false): Point {
    val visited: MutableSet<Point> = mutableSetOf()
    var position = Point()
    var direction = Direction.NORTH

    moves.forEach {
        val turn = it.take(1)
        val distance = it.takeLast(it.length - 1)
        direction = direction.turn(Turn.from(turn))
        val result = position.move(direction, distance.toInt(), visited, trackCollisions)

        position = result.point
        visited.addAll(result.visitedPoints)

        if (trackCollisions) {
            result.previouslyVisited?.let { it2 ->
                return it2
            }
        }
    }

    return position
}

fun manhattanDistance(from: Point, to: Point): Int {
    return abs(from.x - to.x) + abs(from.y - to.y)
}