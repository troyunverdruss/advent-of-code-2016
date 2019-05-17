package days.day20

import readLines


fun parseRange(line: String): LongRange {
    val split = line.split("-")
    return LongRange(split[0].toLong(), split[1].toLong())
}

fun run1(ranges: List<LongRange>): Long {
    var lowestOpen = 0L

    for (range in ranges) {
        if (range.start <= lowestOpen) {
            lowestOpen = range.endInclusive + 1L
        }
    }

    return lowestOpen
}

fun run2(ranges: List<LongRange>, max: Long): Long {
    var lastLowestOpen = 0L
    var totalOpen = 0L

    for (range in ranges) {
//        println("processing range: $range")
        if (range.start <= lastLowestOpen) {
            // Only reset the lastLowestOpen if this range is actually higher than it
            if (lastLowestOpen < range.endInclusive) {
                lastLowestOpen = range.endInclusive + 1L
            }
        } else {
            totalOpen += range.start - lastLowestOpen
            lastLowestOpen = range.endInclusive + 1L
        }
//        println("total open: $totalOpen")
    }

    totalOpen += ((max + 1) - lastLowestOpen)
//    println("total open: $totalOpen")

    return totalOpen
}

// 0 1 2 3 4 5 6 7 8 9
// step 1: 0-2 -> 3
// step 2: 4-7 (start - lowestOpen) -> count + 1
// step 3:

fun main() {
    val lines = readLines("input_d20.txt")
    val ranges = parseRanges(lines)

    val lowestOpen = run1(ranges)
    println("Part 1, lowest open: $lowestOpen")

    val totalOpen = run2(ranges, 4294967295)
    println("Part 2, total open: $totalOpen")
}

fun parseRanges(lines: List<String>) =
        lines.map { parseRange(it) }.sortedBy { it.start }.toList()