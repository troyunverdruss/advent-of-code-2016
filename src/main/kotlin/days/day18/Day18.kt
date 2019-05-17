package days.day18

import java.io.File

enum class TileType(val c: Char) {
    SAFE('.'), TRAP('^');

    companion object {
        fun from(char: Char): TileType {
            return when (char) {
                '.' -> SAFE
                '^' -> TRAP
                else -> throw IllegalArgumentException("Unknown type: $char")
            }
        }
    }
}

data class Point(val x: Int, val y: Int)

fun run(input: String, rowCount: Int): Int {
    val rows = mutableListOf<MutableList<TileType>>()
    val firstRow = mutableListOf(TileType.SAFE)
    input.forEach { char ->
        firstRow.add(TileType.from(char))
    }
    firstRow.add(TileType.SAFE)
    rows.add(firstRow)

    for (row in 1 until rowCount) {
        val nextRow = mutableListOf(TileType.SAFE)
        for (i in 0..rows[row - 1].size - 3) {
            nextRow.add(computeType(rows[row - 1][i], rows[row - 1][i + 1], rows[row - 1][i + 2]))
        }
        nextRow.add(TileType.SAFE)
        rows.add(nextRow)
    }

    return rows.flatten().filter { it == TileType.SAFE }.size - (rowCount * 2)
}

fun computeType(left: TileType, center: TileType, right: TileType): TileType {
    if (left == TileType.TRAP && center == TileType.TRAP && right == TileType.SAFE) return TileType.TRAP

    if (left == TileType.SAFE && center == TileType.TRAP && right == TileType.TRAP) return TileType.TRAP

    if (left == TileType.TRAP && center == TileType.SAFE && right == TileType.SAFE) return TileType.TRAP

    if (left == TileType.SAFE && center == TileType.SAFE && right == TileType.TRAP) return TileType.TRAP

    return TileType.SAFE
}

fun main() {
    val lines = mutableListOf<String>()
    File(ClassLoader.getSystemResource("input_d18.txt").file).forEachLine {
        lines.add(it)
    }

    val safeTiles1 = run(lines[0], 40)
    println("Part 1, number of safe tile: $safeTiles1")

    val safeTiles2 = run(lines[0], 400000)
    println("Part 2, number of safe tile: $safeTiles2")
}