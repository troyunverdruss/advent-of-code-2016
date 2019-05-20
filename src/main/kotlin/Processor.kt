data class Processor(var instructions: List<String>) {
    private val parsedInstructions = mutableListOf<Instruction>()
    private var instructionPosition = 0
    val registers: MutableMap<String, Int> = mutableMapOf<String, Int>().withDefault { 0 }
    var run = true
    var debug = false

    init {
        instructions.forEach { line ->
            parsedInstructions.add(parseInstruction(line))
        }
    }

    fun run() {
        run = true
        while (instructionPosition in 0..instructions.lastIndex) {
            if (debug) {
                print(registers.keys.sorted().map { it to registers[it] }.toList())
                print("     $instructionPosition: ${parsedInstructions[instructionPosition]}     ")
                step()
                println(registers.keys.sorted().map { it to registers[it] }.toList())
            } else {
                step()
            }

            instructionPosition += 1
        }
    }

    private fun step() {
        val instruction = parsedInstructions[instructionPosition]
        when (instruction.command) {
            Command.CPY -> {
                // Only if arg2 is a register should we try and do it
                if (isRegister(instruction.arg2)) {
                    registers[instruction.arg2!!] = getValue(instruction.arg1)
                }
            }
            Command.INC -> {
                if (isRegister(instruction.arg1)) {
                    registers[instruction.arg1] = registers[instruction.arg1]!!.plus(1)
                }
            }
            Command.DEC -> {
                if (isRegister(instruction.arg1)) {
                    registers[instruction.arg1] = registers[instruction.arg1]!!.minus(1)
                }
            }
            Command.JNZ -> {
                if (getValue(instruction.arg1) != 0) {
                    instructionPosition += getValue(instruction.arg2!!)
                    instructionPosition -= 1
                }
            }
            Command.TGL -> {
                val positionToModify = instructionPosition + getValue(instruction.arg1)
                if (positionToModify in 0..parsedInstructions.lastIndex) {
                    val targetedInstruction = parsedInstructions[positionToModify]
                    if (targetedInstruction.argCount() == 1) {
                        if (targetedInstruction.command == Command.INC) {
                            parsedInstructions[positionToModify] = Instruction(Command.DEC, targetedInstruction.arg1, targetedInstruction.arg2)
                        } else {
                            parsedInstructions[positionToModify] = Instruction(Command.INC, targetedInstruction.arg1, targetedInstruction.arg2)
                        }
                    } else {
                        if (targetedInstruction.command == Command.JNZ) {
                            parsedInstructions[positionToModify] = Instruction(Command.CPY, targetedInstruction.arg1, targetedInstruction.arg2)
                        } else {
                            parsedInstructions[positionToModify] = Instruction(Command.JNZ, targetedInstruction.arg1, targetedInstruction.arg2)
                        }
                    }
                } else {
                    if (debug) print("Toggle target outside of instruction set: $positionToModify     ")
                }
            }
        }
    }

    private fun isRegister(value: String?): Boolean {
        return value != null && value.toIntOrNull() == null
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

data class Instruction(val command: Command, val arg1: String, val arg2: String?) {
    fun argCount(): Int {
        return if (arg2 != null) {
            2
        } else {
            1
        }
    }
}

enum class Command {
    CPY, INC, DEC, JNZ, TGL;

    companion object {
        fun from(command: String): Command {
            return valueOf(command.toUpperCase())
        }
    }
}