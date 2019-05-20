package days.day12

import Processor
import java.io.File

interface Value {
    fun getValue(): Int
}

fun main() {
    val lines = mutableListOf<String>()
    File(ClassLoader.getSystemResource("input_d12.txt").file).forEachLine {
        lines.add(it)
    }
    val processor1 = Processor(lines)
    processor1.run()
    println("Part 1, register a contains: ${processor1.registers["a"]}")

    val processor2 = Processor(lines)
    processor2.registers["c"] = 1
    processor2.run()
    println("Part 2, register a contains: ${processor2.registers["a"]}")


}