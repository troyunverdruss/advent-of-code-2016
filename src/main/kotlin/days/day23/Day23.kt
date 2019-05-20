package days.day23

import java.io.File
import Processor

fun main() {
    val lines = mutableListOf<String>()
    File(ClassLoader.getSystemResource("input_d23.txt").file).forEachLine {
        lines.add(it)
    }

    val filteredLines = lines.filter { !it.contains("#") && !it.matches(Regex("^\\s*$")) }.map { it.trim() }

    val processor = Processor(filteredLines)
    processor.registers["a"] = 7
    processor.debug = true
    processor.run()
    println("Part 1, register a contains: ${processor.registers["a"]}")

    // This never actually completes, answer is input (a) factorial plus 85*76
//    val processor2 = Processor(filteredLines)
//    processor2.registers["a"] = 12
//    processor2.debug = true
//    processor2.run()
    println("Part 2, register a contains: ${(1..12).toList().fold(1, { total, next -> total * next }) + 85 * 76}")
}