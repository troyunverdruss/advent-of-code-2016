package days.day02

import java.io.File
import java.lang.IllegalArgumentException

data class DirectionMapping(val up: String?, val down: String?, val left: String?, val right: String?)

class Key(private val mapping: DirectionMapping, private val value: String) {
    fun move(direction: Direction): String {
        return when (direction) {
            Direction.UP -> mapping.up ?: value
            Direction.DOWN -> mapping.down ?: value
            Direction.LEFT -> mapping.left ?: value
            Direction.RIGHT -> mapping.right ?: value
        }
    }
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT;

    companion object {
        fun from(c: Char): Direction {
            return when (c.toUpperCase()) {
                'U' -> UP
                'D' -> DOWN
                'L' -> LEFT
                'R' -> RIGHT
                else -> throw IllegalArgumentException("Unknown character: $c")
            }
        }
    }
}

fun buildKeyToDirectionMappings(layout: Array<Array<String>>): Map<String, Key> {
    val mappings = mutableMapOf<String, Key>()
    for (y in layout.indices) {
        for (x in layout[y].indices) {
            mappings.put(
                    layout[y][x],
                    Key(DirectionMapping(
                            up = getValue(layout, x, y - 1),
                            down = getValue(layout, x, y + 1),
                            left = getValue(layout, x - 1, y),
                            right = getValue(layout, x + 1, y)
                    ), layout[y][x]))

        }
    }
    return mappings
}

fun getValue(layout: Array<Array<String>>, x: Int, y: Int): String? {
    if (y in layout.indices && x in layout[y].indices) {
        return if (layout[y][x] != " ") layout[y][x] else null
    }
    return null
}

fun getLayout(id: Int = 1): Array<Array<String>> {
    return when (id) {
        1 -> arrayOf(
                arrayOf("1", "2", "3"),
                arrayOf("4", "5", "6"),
                arrayOf("7", "8", "9")
        )
        2 -> arrayOf(
                arrayOf(" ", " ", "1", " ", " "),
                arrayOf(" ", "2", "3", "4", " "),
                arrayOf("5", "6", "7", "8", "9"),
                arrayOf(" ", "A", "B", "C", " "),
                arrayOf(" ", " ", "D", " ", " ")
        )
        else -> throw IllegalArgumentException("Illegal ID: $id")
    }
}

fun processLines(lines: MutableList<String>, layoutId: Int = 1): String {
    val mappings = buildKeyToDirectionMappings(getLayout(layoutId))
    var position = "5"
    val sequence: MutableList<String> = mutableListOf()

    lines.forEach { line ->
        line.forEach { char ->
            position = mappings[position]?.move(Direction.from(char)) ?: position
        }
        sequence.add(position)
    }

    val code = sequence.joinToString(separator = "")
    return code
}

fun main() {
    val lines = mutableListOf<String>()
    File(ClassLoader.getSystemResource("input_d2.txt").file).forEachLine {
        lines.add(it)
    }

    val code = processLines(lines)
    println("Part 1, code: $code")

    val code2 = processLines(lines, layoutId = 2)
    println("Part 2, code: $code2")
}
