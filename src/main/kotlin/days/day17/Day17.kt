package days.day17

import md5
import java.lang.IllegalStateException
import java.util.PriorityQueue
import kotlin.math.abs

enum class Direction(val char: Char, val index: Int, val delta: Point) {
    UP('U', 0, Point(0, -1)),
    DOWN('D', 1, Point(0, 1)),
    LEFT('L', 2, Point(-1, 0)),
    RIGHT('R', 3, Point(1, 0))
}

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

data class Room(val loc: Point, val path: String)

fun nextRooms(room: Room, input: String): List<Room> {
    val hash = (input + room.path).md5()

    val results = mutableListOf<Room>()
    Direction.values().forEach { dir ->
        if (hash[dir.index] in "bcdef"
                && (room.loc + dir.delta).x in 0..3
                && (room.loc + dir.delta).y in 0..3) {
            results.add(Room(room.loc + dir.delta, room.path + dir.char))
        }
    }

    return results
}

fun manhattanDistance(a: Point, b: Point): Int {
    return abs(a.x - b.x) + abs(a.y - b.y)
}

fun findShortestPath(initialPoint: Point, input: String): String {
    val queue = PriorityQueue<Room>(compareBy { it.path.length + manhattanDistance(it.loc, Point(3, 3)) })

    val firstRoom = Room(initialPoint, "")
    queue.add(firstRoom)

    while (queue.isNotEmpty()) {
        val room = queue.poll()

        val nextRooms = nextRooms(room, input)
        nextRooms.forEach { nextRoom ->
            if (nextRoom.loc == Point(3, 3)) {
                return nextRoom.path
            }

            queue.add(nextRoom)
        }
    }

    throw IllegalStateException("Unable to find a path to (3, 3)")
}

fun findLongestPathLength(initialPoint: Point, input: String): Int {
    val queue = PriorityQueue<Room>(compareBy { -it.path.length })


    val firstRoom = Room(initialPoint, "")
    queue.add(firstRoom)

    var longestPathLength = 0

    while (queue.isNotEmpty()) {
//        println("queue length: ${queue.size}, longest path length: $longestPathLength")
        val room = queue.poll()

        val nextRooms = nextRooms(room, input)
        nextRooms.forEach { nextRoom ->
            if (nextRoom.loc != Point(3, 3)) {
                queue.add(nextRoom)
            } else if (nextRoom.path.length > longestPathLength) {
                longestPathLength = nextRoom.path.length
            }
        }
    }

    return longestPathLength
}


fun main() {
    val input = "qzthpkfp"
    val path = findShortestPath(Point(0, 0), input)
    println("Part 1 path: $path")

    val longestPath = findLongestPathLength(Point(0, 0), input)
    println("Part 2, longest path length: $longestPath")

}