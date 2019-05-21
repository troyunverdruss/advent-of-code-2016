package days.day22

import AStarSearchQueue
import readLines
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

data class Disk(val id: String, val point: Point, val size: Int, var used: Int) {
    fun avail() = size - used
}

fun parseDisks(lines: List<String>): List<Disk> {
    val disks = mutableListOf<Disk>()
    lines.forEach { line ->
        if (line.contains("/dev/grid/")) {
            val parts = line.split(Regex("\\s+"))
            val point = Regex(".*x(\\d+)-y(\\d+).*").matchEntire(parts[0])?.let { matchResult ->
                Point(matchResult.groups[1]!!.value.toInt(), matchResult.groups[2]!!.value.toInt())
            } ?: throw IllegalArgumentException("Couldn't parse point")


            val size = parts[1].replace("T", "").toInt()
            val used = parts[2].replace("T", "").toInt()
            val avail = parts[3].replace("T", "").toInt()

            if (size - used != avail) {
                throw IllegalStateException("$size - $used != $avail")
            }


            disks.add(Disk(parts[0],
                    point,
                    size,
                    used
            ))
        }
    }

    return disks
}

fun part1(disks: List<Disk>): Int {
    var count = 0
    for (diskB in disks.sortedBy { it.avail() }) {
        for (diskA in disks.sortedBy { it.used }) {
            if (diskA != diskB && diskA.used <= diskB.avail() && diskA.used != 0) {
                count += 1
            }
        }
    }
    return count
}

fun part2(disks: List<Disk>): Int {
    visualizePart2(SearchState(Point(disks.map { it.point.x }.max()!!, 0), disks.sortedBy { it.id }))

//    val emptyDisk = findEmptyDisk(disks)
//    val maxX = disks.map { it.point.x }.max()!!
//
//    var distance = 0
//    // Walk the empty space up to y=0
//    // Then walk the empty space over to x=(max x - 1)
//    distance += distance(emptyDisk.point, Point(maxX - 1, 0))
//    // Then walk the data back to (0,0)
//    distance += (maxX - 1) * 6
//
//    return distance

    val stateAfterBlankToTop = runSearchForGettingBlankToTop(disks)
    val stateAfterBlankToGoalData = runSearchForGettingBlankToGoalData(stateAfterBlankToTop.disks)
    val finalState = runFinalSearch(stateAfterBlankToGoalData.disks)

    return -1
//    return runSearch(disks)
}

private fun findEmptyDisk(disks: List<Disk>): Disk = disks.filter { it.used == 0 }.first()
private fun findEmptyDisks(disks: List<Disk>): List<Disk> = disks.filter { it.used == 0 }

data class SearchState(val goalData: Point, val disks: List<Disk>) {
    private val origin = Point(0, 0)

    fun isSolution(): Boolean {
        return goalData == origin
    }
}

fun estimateDistance(state: SearchState): Int {
    val targetForEmptyDisk = state.goalData + Point(-1, 0)
    val targetForData = Point(0, 0)

    val emptyDisks = findEmptyDisks(state.disks)
    val closestEmpty = emptyDisks.sortedBy { distance(it.point, targetForEmptyDisk) }.first()

    return distance(state.goalData, targetForData) + distance(closestEmpty.point, state.goalData)
}

fun estimateDistanceForBlankToTop(state: SearchState): Int {
    val emptyDisks = findEmptyDisks(state.disks)
    val closestEmpty = emptyDisks.sortedBy { it.point.y }.first()

    return closestEmpty.point.y
}

fun estimateDistanceForBlankToGoalData(state: SearchState): Int {
    val targetForEmptyDisk = state.goalData + Point(-1, 0)

    val emptyDisks = findEmptyDisks(state.disks)
    val closestEmpty = emptyDisks.sortedBy { distance(it.point, targetForEmptyDisk) }.first()

    return distance(closestEmpty.point, targetForEmptyDisk)
}

private fun nextStates(state: SearchState): List<SearchState> {
    val diskMap = state.disks.map { it.point to it }.toMap()
    val diskDirectionPairs = findMovableDirections(diskMap)

    val nextStates = mutableListOf<SearchState>()

    for (pair in diskDirectionPairs) {
        val nextDiskMap = diskMap.map { it.key to it.value.copy() }.toMap()
        val toMove = nextDiskMap[pair.first.point]!!.used
        nextDiskMap[pair.first.point + pair.second]!!.used += toMove
        nextDiskMap[pair.first.point]!!.used = 0

        if (pair.first.point == state.goalData) {
            nextStates.add(SearchState(state.goalData + pair.second, nextDiskMap.values.toList().sortedBy { it.id }))
        } else {
            nextStates.add(SearchState(state.goalData, nextDiskMap.values.toList().sortedBy { it.id }))
        }
    }

    return nextStates
}

fun runSearchForGettingBlankToTop(disks: List<Disk>): SearchState {
    val searchBlankToTop = AStarSearchQueue(::estimateDistanceForBlankToTop)
    val maxX = disks.map { it.point.x }.max()!!
    val initialSearchState = SearchState(Point(maxX, 0), disks.toList().sortedBy { it.id })

    visualizePart2(initialSearchState)

    searchBlankToTop.enqueue(initialSearchState, 0)
    while (searchBlankToTop.isNotEmpty()) {
        val test = searchBlankToTop.open()
        println("searchBlankToTop queue size: ${searchBlankToTop.queueSize()}")

        for (nextState in nextStates(test)) {
            if (estimateDistanceForBlankToTop(nextState) == 0) {
                visualizePart2(nextState)
                println("first: ${searchBlankToTop.getLastItemStepCount() + 1}")
                return nextState
            }
            searchBlankToTop.enqueue(nextState, searchBlankToTop.getLastItemStepCount() + 1)
        }
        searchBlankToTop.close(test)
    }

    throw IllegalStateException("Unable to find solution for runSearchForGettingBlankToTop")
}
// 47  24  181
fun runSearchForGettingBlankToGoalData(disks: List<Disk>): SearchState {
    val searchBlankToTop = AStarSearchQueue(::estimateDistanceForBlankToGoalData)
    val maxX = disks.map { it.point.x }.max()!!
    val initialSearchState = SearchState(Point(maxX, 0), disks.toList().sortedBy { it.id })

    visualizePart2(initialSearchState)

    searchBlankToTop.enqueue(initialSearchState, 0)
    while (searchBlankToTop.isNotEmpty()) {
        val test = searchBlankToTop.open()
        println("searchBlankToTop queue size: ${searchBlankToTop.queueSize()}")

        for (nextState in nextStates(test)) {
            if (estimateDistanceForBlankToGoalData(nextState) == 0) {
                visualizePart2(nextState)
                println("second: ${searchBlankToTop.getLastItemStepCount() + 1}")
                return nextState
            }
            searchBlankToTop.enqueue(nextState, searchBlankToTop.getLastItemStepCount() + 1)
        }
        searchBlankToTop.close(test)
    }

    throw IllegalStateException("Unable to find solution for runSearchForGettingBlankToTop")
}

fun runFinalSearch(disks: List<Disk>): SearchState {
    val searchBlankToTop = AStarSearchQueue(::estimateDistance)
    val maxX = disks.map { it.point.x }.max()!!
    val initialSearchState = SearchState(Point(maxX, 0), disks.toList().sortedBy { it.id })

    visualizePart2(initialSearchState)

    searchBlankToTop.enqueue(initialSearchState, 0)
    while (searchBlankToTop.isNotEmpty()) {
        val test = searchBlankToTop.open()

        if (searchBlankToTop.queueSize() % 1000 == 0) {
            println("searchBlankToTop queue size: ${searchBlankToTop.queueSize()}")
            visualizePart2(test)
        }


        for (nextState in nextStates(test)) {
            if (nextState.goalData == Point(0, 0)) {
                visualizePart2(nextState)
                println("final: ${searchBlankToTop.getLastItemStepCount() + 1}")
                return nextState
            }
            searchBlankToTop.enqueue(nextState, searchBlankToTop.getLastItemStepCount() + 1)
        }
        searchBlankToTop.close(test)
    }

    throw IllegalStateException("Unable to find solution for runSearchForGettingBlankToTop")
}



//val aStarSearchQueue = AStarSearchQueue<SearchState>(::estimateDistance)
//
//val initialSearchState = SearchState(Point(maxX, 0), disks.toList().sortedBy { it.id })
//aStarSearchQueue.enqueue(initialSearchState, 0)
//
//while (aStarSearchQueue.isNotEmpty()) {
//    val test = aStarSearchQueue.open()
//    println("Queue size: ${aStarSearchQueue.queueSize()}")
//
//    // For debugging only!
////        visualizePart2(test)
//
//    for (nextState in nextStates(test)) {
//        if (nextState.isSolution()) {
//            return aStarSearchQueue.getLastItemStepCount() + 1
//        }
//        // For debugging only!
////            visualizePart2(nextState, indent = true)
//        aStarSearchQueue.enqueue(nextState, aStarSearchQueue.getLastItemStepCount() + 1)
//    }
//
//    aStarSearchQueue.close(test)
//}

//throw IllegalStateException("Could not find a valid solution")
//}

private fun distance(a: Point, b: Point): Int {
    return abs(a.x - b.x) + abs(a.y - b.y)
}

private fun visualizePart2(state: SearchState, indent: Boolean = false) {
    val diskMap = state.disks.map { it.point to it }.toMap()
    val maxX = diskMap.map { it.key.x }.max()!!
    val maxY = diskMap.map { it.key.y }.max()!!

    val indentChar = if (indent) {
        "> "
    } else {
        ""
    }

    for (y in 0..maxY) {
        print(indentChar)
        for (x in 0..maxX) {
            if (Point(x, y) == state.goalData) {
                print("G|")
            } else {
                val str = visualizeMovableDirections(diskMap.getValue(Point(x, y)), diskMap)
                print("$str|")
            }
        }
        println()
    }
    println()
}

fun findMovableDirections(diskMap: Map<Point, Disk>): List<Pair<Disk, Point>> {
    val canMove = mutableListOf<Pair<Disk, Point>>()
    val directions = listOf(Point(0, -1), Point(-1, 0), Point(1, 0), Point(0, 1))

    for (disk in diskMap.values) {
        for (dir in directions) {
            if (disk.point + dir in diskMap
                    && disk.used <= diskMap.getValue(disk.point + dir).avail()
                    && disk.used != 0) {
//                println("Can move $disk to ${disk.point + dir}")
                canMove.add(Pair(disk, dir))

            }
        }
    }

    return canMove
}

fun visualizeMovableDirections(node: Disk, diskMap: Map<Point, Disk>): String {
    val sb = StringBuilder()
    var neighbors = 0
    var neighborsTooSmall = 0

    val directions = mapOf(Point(0, -1) to '^', Point(-1, 0) to '<', Point(1, 0) to '>', Point(0, 1) to 'v')

    for (dir in directions) {
        if (node.point + dir.key in diskMap) {
            neighbors += 1
        }

//        if (node.point + dir.key in diskMap && node.used <= diskMap.getValue(node.point + dir.key).avail && node.used != 0) {
//            sb.append(dir.value)
////            sb.append('.')
//        } else if (node.point + dir.key in diskMap && node.used > diskMap.getValue(node.point + dir.key).size) {
//            neighborsTooSmall += 1
//        }
    }
    if (node.used == 0) {
//        sb.clear()
        sb.append('_')
    } else {
        sb.append('.')
    }
//    else if (neighborsTooSmall >= 1) {
//        sb.append("#")
//    } else if (sb.isEmpty()) {
//        sb.append('.')
//    }
    return sb.toString()
}

fun main() {
    val lines = readLines("input_d22.txt")
    val disks = parseDisks(lines)
    val validPairs = part1(disks)
    println("Valid pairs: $validPairs")

    val steps = part2(disks)
    println("Number of steps: $steps")
    // 241 is too low
    // 287 too high
}