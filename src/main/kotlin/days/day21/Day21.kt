package days.day21

import readLines
import java.lang.IllegalStateException


interface Instruction {
    fun process(chars: MutableList<Char>): MutableList<Char>
    fun reverse(chars: MutableList<Char>): MutableList<Char>
}

interface OneArg<T> {
    val x: T
}

interface TwoArg<T> {
    val x: T
    val y: T
}

data class SwapPositionInstruction(override val x: Int, override val y: Int) : Instruction, TwoArg<Int> {
    override fun process(chars: MutableList<Char>): MutableList<Char> {
        val tmp = chars[x]
        chars[x] = chars[y]
        chars[y] = tmp
        return chars
    }

    override fun reverse(chars: MutableList<Char>): MutableList<Char> = process(chars)
}

data class SwapLetterInstruction(override val x: Char, override val y: Char) : Instruction, TwoArg<Char> {
    override fun process(chars: MutableList<Char>): MutableList<Char> {
        val xIndex = chars.indexOf(x)
        val yIndex = chars.indexOf(y)
        return SwapPositionInstruction(xIndex, yIndex).process(chars)
    }

    override fun reverse(chars: MutableList<Char>): MutableList<Char> = process(chars)
}

data class RotateRightInstruction(override val x: Int) : Instruction, OneArg<Int> {
    override fun process(chars: MutableList<Char>): MutableList<Char> {
        val rotate = x % chars.size
        val splitPoint = chars.lastIndex - rotate + 1
        return (chars.slice(splitPoint..chars.lastIndex) + chars.slice(0 until splitPoint)).toMutableList()
    }

    override fun reverse(chars: MutableList<Char>): MutableList<Char> = RotateLeftInstruction(x).process(chars)
}

data class RotateLeftInstruction(override val x: Int) : Instruction, OneArg<Int> {
    override fun process(chars: MutableList<Char>): MutableList<Char> {
        val rotate = x % chars.size
        return (chars.slice(rotate..chars.lastIndex) + chars.slice(0 until rotate)).toMutableList()
    }

    override fun reverse(chars: MutableList<Char>): MutableList<Char> = RotateRightInstruction(x).process(chars)
}

data class RotateByLetterInstruction(override val x: Char) : Instruction, OneArg<Char> {
    override fun process(chars: MutableList<Char>): MutableList<Char> {
        val index = chars.indexOf(x)
        if (index >= 4) {
            return RotateRightInstruction(index + 2).process(chars)
        }
        return RotateRightInstruction(index + 1).process(chars)
    }

    override fun reverse(chars: MutableList<Char>): MutableList<Char> {
        // Let's just brute force this and see what we come up with ...
        for ( i in 0..chars.size) {
            // Rotate our current chars left by 'i'
            val candidate = RotateLeftInstruction(i).process(chars.toMutableList())
            // Then rotate it back by the rules of letter 'x' and see if it lands us with our original
            // argument of 'chars'
            val compareTo = process(candidate.toMutableList())

            if (compareTo == chars) {
                // If so, then our candidate is a good one, let's go with it
                return candidate
            }
        }

        // Uh oh ...
        throw IllegalStateException("Couldn't find a reversal for $chars")
    }
}

data class ReverseInstruction(override val x: Int, override val y: Int) : Instruction, TwoArg<Int> {
    override fun process(chars: MutableList<Char>): MutableList<Char> {
        val slice = chars.slice(x..y)
        var index = x
        for (char in slice.reversed()) {
            chars[index] = char
            index += 1
        }
        return chars
    }

    override fun reverse(chars: MutableList<Char>): MutableList<Char> = process(chars)
}

data class MoveInstruction(override val x: Int, override val y: Int) : Instruction, TwoArg<Int> {
    override fun process(chars: MutableList<Char>): MutableList<Char> {
        val char = chars.removeAt(x)
        chars.add(y, char)
        return chars
    }

    override fun reverse(chars: MutableList<Char>): MutableList<Char> {
        val char = chars.removeAt(y)
        chars.add(x, char)
        return chars
    }
}


class Scrambler(val lines: List<String>) {
    private val instructions: List<Instruction>

    init {
        instructions = lines.map { parseInstruction(it) }.toList()
    }

    private fun parseInstruction(rawInstruction: String): Instruction {
        Regex("swap position (\\d+) with position (\\d+)").matchEntire(rawInstruction)?.let { matchResult ->
            return SwapPositionInstruction(matchResult.groups[1]!!.value.toInt(), matchResult.groups[2]!!.value.toInt())
        }
        Regex("swap letter ([a-z]) with letter ([a-z])").matchEntire(rawInstruction)?.let { matchResult ->
            return SwapLetterInstruction(matchResult.groups[1]!!.value[0], matchResult.groups[2]!!.value[0])
        }
        Regex("rotate right (\\d+) step(s)?").matchEntire(rawInstruction)?.let { matchResult ->
            return RotateRightInstruction(matchResult.groups[1]!!.value.toInt())
        }
        Regex("rotate left (\\d+) step(s)?").matchEntire(rawInstruction)?.let { matchResult ->
            return RotateLeftInstruction(matchResult.groups[1]!!.value.toInt())
        }
        Regex("rotate based on position of letter ([a-z])").matchEntire(rawInstruction)?.let { matchResult ->
            return RotateByLetterInstruction(matchResult.groups[1]!!.value[0])
        }
        Regex("reverse positions (\\d+) through (\\d+)").matchEntire(rawInstruction)?.let { matchResult ->
            return ReverseInstruction(matchResult.groups[1]!!.value.toInt(), matchResult.groups[2]!!.value.toInt())
        }
        Regex("move position (\\d+) to position (\\d+)").matchEntire(rawInstruction)?.let { matchResult ->
            return MoveInstruction(matchResult.groups[1]!!.value.toInt(), matchResult.groups[2]!!.value.toInt())
        }

        throw IllegalArgumentException("Unknown instruction string: $rawInstruction")
    }

    fun scramble(input: String): String {
        var chars = input.toMutableList()
        for (instruction in instructions) {
            chars = instruction.process(chars)
        }
        return chars.joinToString("")
    }

    fun unscramble(input: String): String {
        var chars = input.toMutableList()
        for (instruction in instructions.reversed()) {
            chars = instruction.reverse(chars)
        }
        return chars.joinToString("")
    }

}

fun main() {
    val lines = readLines("input_d21.txt")
    val scrambler = Scrambler(lines)
    val scrambledResult = scrambler.scramble("abcdefgh")
    println("Part 1, $scrambledResult")

    val unscrambledResult = scrambler.unscramble("fbgdceah")
    println("Part 2, $unscrambledResult")
}