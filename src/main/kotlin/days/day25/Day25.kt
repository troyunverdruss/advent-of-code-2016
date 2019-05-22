package days.day25

import readLines
import Processor

fun main() {
    val lines = readLines("input_d25.txt")
    val filteredLines = lines.filter { !it.contains("#") && !it.matches(Regex("^\\s*$")) }.map { it.trim() }
    val processor = Processor(filteredLines)
//    processor.registers["a"] = 362 + 1

//    processor.registers["a"] = 7
//    processor.registers["b"] = -1
//    processor.registers["c"] = -1
//    processor.registers["d"] = -1
//    processor.instructionPosition = 6
//    processor.debug = true
//    processor.run()

    val processor0 = Processor(filteredLines)
    processor0.registers["a"] = 0
    processor0.run()

    val processor1 = Processor(filteredLines)
    processor1.registers["a"] = 1
    processor1.run()

    val processor2 = Processor(filteredLines)
    processor2.registers["a"] = 50
    processor2.run()

    val processor3 = Processor(filteredLines)
    processor3.registers["a"] = 100
    processor3.run()

    val i = 0

    // 196
}