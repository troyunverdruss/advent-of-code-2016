package days.day11

import java.util.concurrent.TimeUnit
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

data class Node<T>(val data: T, var parent: Node<T>?) {
    val children: MutableSet<Node<T>> = mutableSetOf()
    val depth: Int = parent?.depth?.plus(1) ?: 0

    init {
        parent?.let {
            parent!!.children.add(this)
        }
    }

    fun root(): Node<T> {
        return parent?.root() ?: this
    }

    fun ancestors(): Set<Node<T>> {
        parent?.let {
            return parent!!.ancestors().plus(this.parent!!)
        }
        return setOf()
    }

    fun descendants(): Set<Node<T>> {
        return children.plus(children.flatMap { it.descendants() })
    }

    fun siblings(): Set<Node<T>> {
        return parent?.children?.minus(this) ?: setOf()
    }
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

fun run(initialState: State): Int {
//    val queue = LinkedList<State>()
    val queue = LinkedHashSet<State>()

    queue.add(initialState)

    var seekingSolution = true
    var solutionNode: State? = null
    val seen = mutableSetOf<Int>()
    seen.add(queue.first().hashCode())

    var level = 0
    val seenLastLevel = mutableSetOf<Int>()

    while (seekingSolution) {
        level += 1
        print(level)
        val seenThisLevel = mutableSetOf<Int>()

        val elapsedMillis = measureTimeMillis {
            for (i in 0 until queue.size) {
                val parentState = queue.first()
                queue.remove(parentState)

                val possibleNextStates = parentState.nextStates()
                val validStates = possibleNextStates.filter { it.isValid() }

                if (validStates.isEmpty()) {
//                parentState.parent?.children?.remove(parentState)
                    continue
                }

                for (possibleNextState in validStates) {
//                val possibleChildNode = Node(possibleNextState, parentState)

//                if (possibleNextState.isValid()) { // && !seen.contains(possibleChildNode.hashCode())) {
                    if (!seen.contains(possibleNextState.hashCode())) {
                        queue.add(possibleNextState)
                        seenThisLevel.add(possibleNextState.hashCode())

                        if (possibleNextState.isEndState()) {
//                        solutionNode = possibleNextState
                            seekingSolution = false
                            break
                        }
                    }
//                }
                }
            }
        }

//        seen.addAll(seenThisLevel.map { it.hashCode() })

        // Pruning all the objects above the current level ...
//        seenThisLevel.forEach { node -> node.parent = null }
//        seenLastLevel.clear()
//        seenLastLevel.addAll(seenThisLevel)
//        seen.addAll(seenThisLevel)
        println(": ${seenThisLevel.size} (${elapsedMillis}ms)")
        seenThisLevel.clear()
    }

    return level
}

fun main() {
    val inputPart1 = getPuzzleInputPart1()
    val result = run(inputPart1)
    println("Part 1, steps: $result")

//    val inputPart2 = getPuzzleInputPart2(inputPart1)
//    val result2 = run(inputPart2)
//    println("Part 2: steps: $result2")

}