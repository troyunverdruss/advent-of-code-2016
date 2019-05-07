package day01

import java.util.Optional

data class MoveResult(val point: Point, val visitedPoints: Set<Point>, val previouslyVisited: Optional<Point>)

data class Point(val x: Int = 0, val y: Int = 0) {
    fun move(direction: Direction,
             distance: Int,
             visited: Set<Point> = setOf(),
             trackCollisions: Boolean = false): MoveResult {

        val justVisited = mutableSetOf<Point>()
        var lastPoint = this
        var previouslyVisited: Point? = null

        for (i in 1..distance) {
            val point = when (direction) {
                Direction.NORTH -> Point(lastPoint.x, lastPoint.y + 1)
                Direction.SOUTH -> Point(lastPoint.x, lastPoint.y - 1)
                Direction.WEST -> Point(lastPoint.x - 1, lastPoint.y)
                Direction.EAST -> Point(lastPoint.x + 1, lastPoint.y)
            }

            if (trackCollisions && visited.contains(point)) {
                println("Collided at $point")
                previouslyVisited = point
            }

            justVisited.add(point)
            lastPoint = point
        }

        return MoveResult(lastPoint, justVisited, Optional.ofNullable(previouslyVisited))
    }
}