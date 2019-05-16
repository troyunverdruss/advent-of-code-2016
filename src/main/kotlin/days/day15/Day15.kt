package days.day15

import java.io.File

data class Disc(val height: Int, val positions: Int, val initalPosition: Int) {
    fun slotAt(timeWhenDropped: Int): Int {
        return (initalPosition + timeWhenDropped + height) % positions
    }
}

fun parseInput(lines: List<String>): List<Disc> {
    val discs = mutableListOf<Disc>()
    lines.forEach { line ->
        Regex("Disc #(\\d+) has (\\d+) positions; at time=0, it is at position (\\d+)\\.").matchEntire(line)?.let { matchResult ->
            discs.add(Disc(
                    matchResult.groups[1]!!.value.toInt(),
                    matchResult.groups[2]!!.value.toInt(),
                    matchResult.groups[3]!!.value.toInt()
            ))
        }
    }
    return discs
}

fun run(discs: List<Disc>): Int {
    var i = 0
    while (true) {
        if (discs.map { it.slotAt(i) }.sum() == 0) {
            return i
        }
        i += 1
    }
}

fun main() {
    val lines = mutableListOf<String>()
    File(ClassLoader.getSystemResource("input_d15.txt").file).forEachLine {
        lines.add(it)
    }

    val discs = parseInput(lines)
    val time1 = run(discs)
    println("Part 1, first time of alignment: $time1")

    val time2 = run(discs.plus(Disc(discs.size + 1, 11, 0)))
    println("Part 2, first time of alignment: $time2")

//Disc #1 has 17 positions; at time=0, it is at position 1.
//Disc #2 has 7 positions; at time=0, it is at position 0.
//Disc #3 has 19 positions; at time=0, it is at position 2.
//Disc #4 has 5 positions; at time=0, it is at position 0.
//Disc #5 has 3 positions; at time=0, it is at position 0.
//Disc #6 has 13 positions; at time=0, it is at position 5.
// 133245 too low

}