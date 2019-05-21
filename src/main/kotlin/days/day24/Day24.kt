package days.day24

import com.google.common.collect.Collections2
import days.day22.Point
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleGraph
import org.jgrapht.graph.SimpleWeightedGraph
import readLines

data class MapWithPointsOfInterest(val map: Map<Point, Char>, val startingPoint: Point, val pointsOfInterest: Map<Char, Point>)

fun parseInput(lines: List<String>): MapWithPointsOfInterest {
    val map = mutableMapOf<Point, Char>()
    val pointsOfInterest = mutableMapOf<Char, Point>()

    var y = 0
    lines.forEach { line ->
        var x = 0
        line.forEach { char ->
            if (char in "01234567") {
                map[Point(x, y)] = '.'
                pointsOfInterest[char] = Point(x, y)
            } else {
                map[Point(x, y)] = char
            }
            x += 1
        }
        y += 1
    }

    return MapWithPointsOfInterest(map, pointsOfInterest['0']!!, pointsOfInterest)
}

fun buildGraph(map: Map<Point, Char>): SimpleGraph<Point, DefaultEdge> {
    val minX = map.keys.map { it.x }.min() ?: throw IllegalStateException("Couldn't find value")
    val minY = map.keys.map { it.y }.min() ?: throw IllegalStateException("Couldn't find value")
    val maxX = map.keys.map { it.x }.max() ?: throw IllegalStateException("Couldn't find value")
    val maxY = map.keys.map { it.y }.max() ?: throw IllegalStateException("Couldn't find value")

    val graph = SimpleGraph<Point, DefaultEdge>(DefaultEdge::class.java)

    for (y in minY..maxY) {
        for (x in minX..maxX) {
            val point = Point(x, y)
            if (map.getValue(point) == '.') {
                graph.addVertex(Point(x, y))

                for (dir in listOf(Point(0, -1), Point(-1, 0))) {
                    if (graph.containsVertex(point + dir)) {
                        graph.addEdge(point, point + dir)
                    }
                }
            }
        }
    }

    return graph
}

fun compute(mapWithPointsOfInterest: MapWithPointsOfInterest, withReturnToStart: Boolean = false): Int {
    val inputGraph = buildGraph(mapWithPointsOfInterest.map)
    val pointsOfInterestGraph = SimpleWeightedGraph<Char, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)

    val points = mapWithPointsOfInterest.pointsOfInterest.toList().sortedBy { it.first }
    points.forEach { pointsOfInterestGraph.addVertex(it.first) }

    val distanceLookup = mutableMapOf<Pair<Char, Char>, Int>()

    val dijkstraShortestPath = DijkstraShortestPath(inputGraph)
    for (a in points) {
        val sublistStart = points.indexOf(a) + 1
        if (sublistStart >= points.size) {
            continue
        }

        for (b in points.subList(points.indexOf(a) + 1, points.size)) {

            val path = dijkstraShortestPath.getPath(a.second, b.second)
//            println("adding edge: ${a.first} -> ${b.first} : ${path.length}")
            pointsOfInterestGraph.addEdge(a.first, b.first)
            pointsOfInterestGraph.setEdgeWeight(a.first, b.first, path.length.toDouble())
            distanceLookup[Pair(a.first, b.first)] = path.length
        }
    }

    var shortestPath = Int.MAX_VALUE
    for (permutation in Collections2.permutations(points.map { it.first }.filter { it != '0' })) {

        var distance = 0
        var last = '0'

        for (next in permutation) {
            val sortedItems = listOf(last, next).sorted()
            val low = sortedItems[0]
            val high = sortedItems[1]

            distance += distanceLookup.getValue(Pair(low, high))
            last = next
        }

        if (withReturnToStart) {
            distance += distanceLookup.getValue(Pair('0', last))
        }

        if (distance < shortestPath) {
            shortestPath = distance
        }
    }

    return shortestPath
}

fun main() {
    val lines = readLines("input_d24.txt")
    val mapWithPointsOfInterest = parseInput(lines)

    val part1Result = compute(mapWithPointsOfInterest)
    println("Part 1, shortest path: $part1Result")

    val part2Result = compute(mapWithPointsOfInterest, true)
    println("Part 2, shortest path: $part2Result")
}