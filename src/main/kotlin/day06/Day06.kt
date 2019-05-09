package day06

import java.io.File

fun parseLinesToGroups(lines: List<String>): Array<MutableList<Char>> {
    val groups = Array<MutableList<Char>>(lines[0].length) { mutableListOf() }

    lines.forEach { line ->
        line.forEachIndexed { index, char ->
            groups[index].add(char)
        }
    }

    return groups
}

private enum class Frequency(val sortModifier: Int) {
    MOST_FREQUENT(-1), LEAST_FREQUENT(1)
}

private fun decodeMessage(groups: Array<MutableList<Char>>, freq: Frequency = Frequency.MOST_FREQUENT): String {
    val sb = StringBuilder()

    groups.forEach { list ->
        val mostFrequent = list.groupingBy { it }
                .eachCount()
                .toList()
                .sortedWith(compareBy { freq.sortModifier * it.second })
                .first()
                .first
        sb.append(mostFrequent)
    }
    return sb.toString()
}

fun main() {
    val lines = mutableListOf<String>()
    File(ClassLoader.getSystemResource("input_d6.txt").file).forEachLine {
        lines.add(it)
    }

    val groups = parseLinesToGroups(lines)
    val mostFrequentDecoding = decodeMessage(groups, Frequency.MOST_FREQUENT)
    val leastFrequentDecoding = decodeMessage(groups, Frequency.LEAST_FREQUENT)

    println("Part 1, decoded phrase: $mostFrequentDecoding")
    println("Part 2, decoded phrase: $leastFrequentDecoding")
}
