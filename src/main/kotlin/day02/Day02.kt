package day02

import java.io.File
import java.lang.IllegalArgumentException

interface Key {
    fun move(direction: Direction): Key
    fun value(): Int
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

object One : Key {
    override fun value(): Int {
        return 1
    }

    override fun move(direction: Direction): Key {
        return when (direction) {
            Direction.UP -> this
            Direction.DOWN -> Four
            Direction.LEFT -> this
            Direction.RIGHT -> Two
        }
    }
}

object Two : Key {
    override fun value(): Int {
        return 2
    }

    override fun move(direction: Direction): Key {
        return when (direction) {
            Direction.UP -> this
            Direction.DOWN -> Five
            Direction.LEFT -> One
            Direction.RIGHT -> Three
        }
    }
}

object Three : Key {
    override fun value(): Int {
        return 3
    }

    override fun move(direction: Direction): Key {
        return when (direction) {
            Direction.UP -> this
            Direction.DOWN -> Six
            Direction.LEFT -> Two
            Direction.RIGHT -> this
        }
    }
}

object Four : Key {
    override fun value(): Int {
        return 4
    }

    override fun move(direction: Direction): Key {
        return when (direction) {
            Direction.UP -> One
            Direction.DOWN -> Seven
            Direction.LEFT -> this
            Direction.RIGHT -> Five
        }
    }
}

object Five : Key {
    override fun value(): Int {
        return 5
    }

    override fun move(direction: Direction): Key {
        return when (direction) {
            Direction.UP -> Two
            Direction.DOWN -> Eight
            Direction.LEFT -> Four
            Direction.RIGHT -> Six
        }
    }
}

object Six : Key {
    override fun value(): Int {
        return 6
    }

    override fun move(direction: Direction): Key {
        return when (direction) {
            Direction.UP -> Three
            Direction.DOWN -> Nine
            Direction.LEFT -> Five
            Direction.RIGHT -> this
        }
    }
}

object Seven : Key {
    override fun value(): Int {
        return 7
    }

    override fun move(direction: Direction): Key {
        return when (direction) {
            Direction.UP -> Four
            Direction.DOWN -> this
            Direction.LEFT -> this
            Direction.RIGHT -> Eight
        }
    }
}

object Eight : Key {
    override fun value(): Int {
        return 8
    }

    override fun move(direction: Direction): Key {
        return when (direction) {
            Direction.UP -> Five
            Direction.DOWN -> this
            Direction.LEFT -> Seven
            Direction.RIGHT -> Nine
        }
    }
}

object Nine : Key {
    override fun value(): Int {
        return 9
    }

    override fun move(direction: Direction): Key {
        return when (direction) {
            Direction.UP -> Six
            Direction.DOWN -> this
            Direction.LEFT -> Eight
            Direction.RIGHT -> this
        }
    }
}

fun processLines(lines: MutableList<String>): String {
    var position: Key = Five
    val sequence: MutableList<Key> = mutableListOf()

    lines.forEach { line ->
        line.forEach { char ->
            position = position.move(Direction.from(char))
        }
        sequence.add(position)
    }

    val code = sequence.map { key -> key.value() }.toCollection(mutableListOf()).joinToString(separator = "")
    return code
}

fun main() {
    val lines = mutableListOf<String>()
    File(ClassLoader.getSystemResource("input_d2.txt").file).forEachLine {
        lines.add(it)
    }

    val code = processLines(lines)
    println("Part 1, code: $code")
}
