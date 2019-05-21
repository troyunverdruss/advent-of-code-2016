package days.day11

import java.util.PriorityQueue
import kotlin.system.measureTimeMillis


interface Component {
    val type: ComponentType
    val id: String
}

enum class ComponentType {
    GENERATOR, MICROCHIP
}

data class Generator(override val id: String) : Component {
    override val type: ComponentType
        get() = ComponentType.GENERATOR

}

data class Microchip(override val id: String) : Component {
    override val type: ComponentType
        get() = ComponentType.MICROCHIP
}

data class State(val elevatorFloor: Int,
                 val floors: List<Set<Component>>) {

    fun estimatedDistanceToCompletion(): Int {
        val max = floors.lastIndex
        var sum = 0
        floors.forEachIndexed { index, floor ->
            sum += (max - index) * floor.size
        }
        return sum
    }

    fun isValid(): Boolean {
        val floorChecks = mutableListOf<Boolean>()
        for (floor in floors) {
            val microchips = floor.filter { it.type == ComponentType.MICROCHIP }.toMutableList()
            val generators = floor.filter { it.type == ComponentType.GENERATOR }

            // Removing all the microchips that are safe because they're with their own generators
            generators.forEach { generator ->
                microchips.removeIf { it.id == generator.id }
            }

            // If all the microchips are paired (or there were none) then we're safe
            // OR if there were no generators then we're safe
            floorChecks.add(microchips.isEmpty() || generators.isEmpty())
        }

        // If any of the floors are not valid (false) then the data is totally invalid
        return floorChecks.all { it }
    }

    fun hashableDetails(): List<Int> {
        val details = mutableListOf<Int>()

        for (floor in floors) {
            var pairCount = 0

            val microchips = floor.filter { it.type == ComponentType.MICROCHIP }
            val generators = floor.filter { it.type == ComponentType.GENERATOR }

            // Removing all the microchips that are safe because they're with their own generators
            generators.forEach { generator ->
                microchips.forEach { microchip ->
                    if (microchip.id == generator.id) {
                        pairCount += 1
                    }
                }
            }

            details.add(pairCount)
            details.add(microchips.size - pairCount)
            details.add(generators.size - pairCount)
        }

        return details
    }


    fun isEndState(): Boolean {
        return floors[0].isEmpty() && floors[1].isEmpty() && floors[2].isEmpty() && isValid()
    }

    fun nextStates(): Set<State> {
        val states = mutableSetOf<State>()

        listOf(elevatorFloor + 1, elevatorFloor - 1).filter { it in 0..3 }.forEach { nextFloor ->
            // This moves each individual item up/down 1 floor as possible
            floors[elevatorFloor].forEach { component ->
                val nextFloors = floors.map { floor -> floor.map { it }.toMutableSet() }.toMutableList()
                nextFloors[nextFloor].add(component)
                nextFloors[elevatorFloor].remove(component)
                val nextState = State(nextFloor, nextFloors)
                states.add(nextState)
            }

            // This moves each possible pair of items up/down 1 floor as possible
            combinationsOf2(floors[elevatorFloor]).forEach { componentPair ->
                val nextFloors = floors.map { floor -> floor.map { it }.toMutableSet() }.toMutableList()
                nextFloors[nextFloor].addAll(componentPair)
                nextFloors[elevatorFloor].removeAll(componentPair)
                val nextState = State(nextFloor, nextFloors)
                states.add(nextState)
            }
        }

        return states
    }
}

fun <T> combinationsOf2(input: Collection<T>): Set<Set<T>> {
    val results = mutableSetOf<Set<T>>()

    val inputList = input.toList()

    for (i in 0 until inputList.size) {
        for (j in i + 1 until inputList.size) {
            results.add(setOf(inputList[i], inputList[j]))
        }
    }

    return results
}

fun getPuzzleInputPart1(): State {
    return State(0,
            listOf(
                    setOf(Generator("promethium"), Microchip("promethium")),
                    setOf(Generator("cobalt"), Generator("curium"), Generator("ruthenium"), Generator("plutonium")),
                    setOf(Microchip("cobalt"), Microchip("curium"), Microchip("ruthenium"), Microchip("plutonium")),
                    setOf()

            )
    )
}

fun getPuzzleInputPart2(inputPart1: State): State {
    return State(0,
            listOf(
                    inputPart1.floors[0].plus(setOf(Generator("elerium"), Microchip("elerium"), Generator("dilithium"), Microchip("dilithium"))),
                    inputPart1.floors[1],
                    inputPart1.floors[2],
                    inputPart1.floors[3]
            )
    )
}

data class SearchNode(val g: Int, val h: Int, val state: State) {
    private val details = state.hashableDetails()

    fun f(): Int {
        return g + h
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchNode

        if (g != other.g) return false
        if (h != other.h) return false
        if (details != other.details) return false

        return true
    }

    override fun hashCode(): Int {
        var result = g
        result = 31 * result + h
        result = 31 * result + details.hashCode()
        return result
    }
}

fun run(initialState: State): Int {
    val open = PriorityQueue<SearchNode>(compareBy { node -> node.f() })
    val inOpen = mutableMapOf<Int, Int>() // SearchNode.hashCode -> f
    val closed = mutableMapOf<Int, Int>() // SearchNode.hashCode -> f

    val initialSearchNode = SearchNode(0, 0, initialState)
    open.add(initialSearchNode)
    inOpen[initialSearchNode.hashCode()] = initialSearchNode.f()

    while (!open.isEmpty()) {
//        println("open: ${open.size}, ${inOpen.size}, closed: ${closed.size}")

        val q = open.poll()
        inOpen.remove(q.hashCode())

        val possibleNextStates = q.state.nextStates()

        for (possibleNextState in possibleNextStates) {
            // We've hit the end, return immediately
            if (possibleNextState.isEndState()) {
                return q.g + 1
            }

            if (possibleNextState.isValid()) {
                val g = q.g + 1
                val h = possibleNextState.estimatedDistanceToCompletion()
                val searchNode = SearchNode(g, h, possibleNextState)

                if (inOpen.containsKey(searchNode.hashCode()) && inOpen[searchNode.hashCode()]!! < searchNode.f()) {
                    continue
                }

                if (closed.containsKey(searchNode.hashCode()) && closed[searchNode.hashCode()]!! < searchNode.f()) {
                    continue
                }

                if (!inOpen.containsKey(searchNode.hashCode())) {
                    open.add(searchNode)
                    inOpen[searchNode.hashCode()] = searchNode.f()
                }
            }
        }

        closed[q.hashCode()] = q.f()
    }

    throw RuntimeException("Could not find a solution")
}

fun main() {
    val inputPart1 = getPuzzleInputPart1()

    val time1 = measureTimeMillis {
        val result1 = run(inputPart1)
        print("Part 1, steps: $result1")
    }
    println(" (${time1}ms)")

    val inputPart2 = getPuzzleInputPart2(inputPart1)
    val time2 = measureTimeMillis {
        val result2 = run(inputPart2)
        print("Part 2: steps: $result2")
    }
    println(" (${time2}ms)")
}