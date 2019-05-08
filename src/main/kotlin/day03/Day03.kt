package day03

import java.io.File
import java.lang.IllegalStateException

fun countValidTrianglesByRows(lines: List<String>): Int {
    var count = 0

    lines.forEach { line ->
        val values = line.split(Regex("\\s+"))
                .filter { it != "" }
                .map { it.toInt() }

        val max = values.max() ?: throw IllegalStateException("No max found in: $values")
        val sum = values.sum() - max

        if (sum > max) count += 1
    }

    return count
}

fun groupByColumns(lines: List<String>): List<List<Int>> {
    val groups = mutableListOf<List<Int>>()

    for (i in lines.indices step 3) {
        val one = mutableListOf<Int>()
        val two = mutableListOf<Int>()
        val three = mutableListOf<Int>()

        for (j in i..i + 2) {
            val values = lines[j].split(Regex("\\s+"))
                    .filter { it != "" }
                    .map { it.toInt() }
            one.add(values[0])
            two.add(values[1])
            three.add(values[2])
        }

        groups.add(one)
        groups.add(two)
        groups.add(three)
    }

    return groups
}

fun countValidTrianglesByColumns(groups: List<List<Int>>): Int {
    var count = 0

    groups.forEach { group ->
        val max = group.max() ?: throw IllegalStateException("No max found in $group")
        val sum = group.sum() - max

        if (sum > max) count += 1
    }

    return count
}

fun main() {
    val lines = mutableListOf<String>()
    File(ClassLoader.getSystemResource("input_d3.txt").file).forEachLine {
        lines.add(it)
    }

    val count = countValidTrianglesByRows(lines)
    println("Part 1, code: $count")

    val groups = groupByColumns(lines)
    val count2 = countValidTrianglesByColumns(groups)
    println("Part 2, code: $count2")

}