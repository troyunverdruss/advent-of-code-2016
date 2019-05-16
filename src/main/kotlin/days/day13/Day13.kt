package days.day13

import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph
import org.jgrapht.traverse.ClosestFirstIterator

data class Point(val x: Int, val y: Int)

enum class Type {
    OPEN, WALL;

    companion object {
        fun from(point: Point, input: Int): Type {
            val decimal = (point.x * point.x) + (3 * point.x) + (2 * point.x * point.y) + point.y + (point.y * point.y) + input
            val binary = decimal.toString(2)
            val count = binary.count { it == '1' }

            return if (count % 2 == 0) {
                OPEN
            } else {
                WALL
            }
        }
    }
}

fun part1(input: Int, target: Point): Int {
    // Build the graph with some buffer
    val graph = SimpleGraph<Point, DefaultEdge>(DefaultEdge::class.java)


    buildGraph(target, input, graph)

    val dijkstraShortestPath = DijkstraShortestPath(graph)
    val path = dijkstraShortestPath.getPath(Point(1, 1), target)
    return path.length
}

fun part2(input: Int): Int {
    // Build the graph with some buffer
    val graph = SimpleGraph<Point, DefaultEdge>(DefaultEdge::class.java)
    val points = mutableMapOf<Point, Type>()

    buildGraph(Point(31, 31), input, graph)

    val closestFirstIterator = ClosestFirstIterator(graph, Point(1, 1), 50.0)

    var count = 0
    closestFirstIterator.forEach { count += 1 }
    return count
}

private fun buildGraph(target: Point, input: Int, graph: SimpleGraph<Point, DefaultEdge>) {
    val points = mutableMapOf<Point, Type>()

    for (x in 0..target.x + 20) {
        for (y in 0..target.y + 20) {
            val point = Point(x, y)
            points[point] = Type.from(point, input)

            // Attach it to neighbors if warranted
            points[point]?.let { type ->
                if (type == Type.OPEN) {
                    graph.addVertex(point)
                    val leftNeighbor = Point(point.x - 1, point.y)
                    val upNeighbor = Point(point.x, point.y - 1)
                    if (leftNeighbor in points && points[leftNeighbor] == Type.OPEN) {
                        graph.addEdge(point, leftNeighbor)
                    }
                    if (upNeighbor in points && points[upNeighbor] == Type.OPEN) {
                        graph.addEdge(point, upNeighbor)
                    }
                }
            }
        }
    }
}

fun main() {
    val input = 1362

    val shortestDistance = part1(input, Point(31, 39))
    println("Part 1, shortest distance to (31, 39): $shortestDistance")

    val countWithin50 = part2(input)
    println("Part 2, open points within 50 steps: $countWithin50")

}