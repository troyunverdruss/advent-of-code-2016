package days.day12

import java.io.File

interface Value {
    fun getValue(): Int
}

enum class Command {
    CPY, INC, DEC, JNZ;

    companion object {
        fun from(command: String): Command {
            return valueOf(command.toUpperCase())
        }
    }
}

data class Instruction(val command: Command, val arg1: String, val arg2: String?)

data class Processor(var instructions: List<String>) {
    var parsedInstructions = mutableListOf<Instruction>()
    var instructionPosition = 0
    val registers: MutableMap<String, Int> = mutableMapOf<String, Int>().withDefault { 0 }
    var run = true

    init {
        instructions.forEach { line ->
            parsedInstructions.add(parseInstruction(line))
        }
    }

    fun run() {
        run = true
        while (instructionPosition in 0..instructions.lastIndex) {
            step()
            instructionPosition += 1
        }
    }

    private fun step() {
        val instruction = parseInstruction(instructions[instructionPosition])
        when (instruction.command) {
            Command.CPY -> registers[instruction.arg2!!] = getValue(instruction.arg1)
            Command.INC -> registers[instruction.arg1] = registers[instruction.arg1]!!.plus(1)
            Command.DEC -> registers[instruction.arg1] = registers[instruction.arg1]!!.minus(1)
            Command.JNZ -> {
                if (getValue(instruction.arg1) != 0) {
                    instructionPosition += getValue(instruction.arg2!!)
                    instructionPosition -= 1
                }
            }
        }
    }

    private fun parseInstruction(instruction: String): Instruction {
        val parts = instruction.split(Regex("\\s+"))
        val command = Command.from(parts[0])
        val arg1 = parts[1]
        val arg2 = if (parts.lastIndex == 2) {
            parts[2]
        } else {
            null
        }

        return Instruction(command, arg1, arg2)
    }

    private fun getValue(intOrRegister: String): Int {
        return if (intOrRegister.toIntOrNull() == null) {
            if (intOrRegister !in registers) registers[intOrRegister] = 0

            registers[intOrRegister] ?: throw IllegalArgumentException("Unknown register: $intOrRegister")
        } else {
            intOrRegister.toInt()
        }
    }
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